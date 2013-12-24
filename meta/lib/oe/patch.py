import oe.path

class NotFoundError(bb.BBHandledException):
    def __init__(self, path):
        self.path = path

    def __str__(self):
        return "Error: %s not found." % self.path

class CmdError(bb.BBHandledException):
    def __init__(self, exitstatus, output):
        self.status = exitstatus
        self.output = output

    def __str__(self):
        return "Command Error: exit status: %d  Output:\n%s" % (self.status, self.output)


def runcmd(args, dir = None):
    import pipes

    if dir:
        olddir = os.path.abspath(os.curdir)
        if not os.path.exists(dir):
            raise NotFoundError(dir)
        os.chdir(dir)
        # print("cwd: %s -> %s" % (olddir, dir))

    try:
        args = [ pipes.quote(str(arg)) for arg in args ]
        cmd = " ".join(args)
        # print("cmd: %s" % cmd)
        (exitstatus, output) = oe.utils.getstatusoutput(cmd)
        if exitstatus != 0:
            raise CmdError(exitstatus >> 8, output)
        return output

    finally:
        if dir:
            os.chdir(olddir)

class PatchError(Exception):
    def __init__(self, msg):
        self.msg = msg

    def __str__(self):
        return "Patch Error: %s" % self.msg

class PatchSet(object):
    defaults = {
        "strippath": 1
    }

    def __init__(self, dir, d):
        self.dir = dir
        self.d = d
        self.patches = []
        self._current = None

    def current(self):
        return self._current

    def Clean(self):
        """
        Clean out the patch set.  Generally includes unapplying all
        patches and wiping out all associated metadata.
        """
        raise NotImplementedError()

    def Import(self, patch, force):
        if not patch.get("file"):
            if not patch.get("remote"):
                raise PatchError("Patch file must be specified in patch import.")
            else:
                patch["file"] = bb.fetch2.localpath(patch["remote"], self.d)

        for param in PatchSet.defaults:
            if not patch.get(param):
                patch[param] = PatchSet.defaults[param]

        if patch.get("remote"):
            patch["file"] = bb.data.expand(bb.fetch2.localpath(patch["remote"], self.d), self.d)

        patch["filemd5"] = bb.utils.md5_file(patch["file"])

    def Push(self, force):
        raise NotImplementedError()

    def Pop(self, force):
        raise NotImplementedError()

    def Refresh(self, remote = None, all = None):
        raise NotImplementedError()


class PatchTree(PatchSet):
    def __init__(self, dir, d):
        PatchSet.__init__(self, dir, d)
        self.patchdir = os.path.join(self.dir, 'patches')
        self.seriespath = os.path.join(self.dir, 'patches', 'series')
        bb.utils.mkdirhier(self.patchdir)

    def _appendPatchFile(self, patch, strippath):
        with open(self.seriespath, 'a') as f:
            f.write(os.path.basename(patch) + "," + strippath + "\n")
        shellcmd = ["cat", patch, ">" , self.patchdir + "/" + os.path.basename(patch)]
        runcmd(["sh", "-c", " ".join(shellcmd)], self.dir)

    def _removePatch(self, p):
        patch = {}
        patch['file'] = p.split(",")[0]
        patch['strippath'] = p.split(",")[1]
        self._applypatch(patch, False, True)

    def _removePatchFile(self, all = False):
        if not os.path.exists(self.seriespath):
            return
        patches = open(self.seriespath, 'r+').readlines()
        if all:
            for p in reversed(patches):
                self._removePatch(os.path.join(self.patchdir, p.strip()))
            patches = []
        else:
            self._removePatch(os.path.join(self.patchdir, patches[-1].strip()))
            patches.pop()
        with open(self.seriespath, 'w') as f:
            for p in patches:
                f.write(p)
         
    def Import(self, patch, force = None):
        """"""
        PatchSet.Import(self, patch, force)

        if self._current is not None:
            i = self._current + 1
        else:
            i = 0
        self.patches.insert(i, patch)

    def _applypatch(self, patch, force = False, reverse = False, run = True):
        shellcmd = ["cat", patch['file'], "|", "patch", "-p", patch['strippath']]
        if reverse:
            shellcmd.append('-R')

        if not run:
            return "sh" + "-c" + " ".join(shellcmd)

        if not force:
            shellcmd.append('--dry-run')

        output = runcmd(["sh", "-c", " ".join(shellcmd)], self.dir)

        if force:
            return

        shellcmd.pop(len(shellcmd) - 1)
        output = runcmd(["sh", "-c", " ".join(shellcmd)], self.dir)

        if not reverse:
            self._appendPatchFile(patch['file'], patch['strippath'])

        return output

    def Push(self, force = False, all = False, run = True):
        bb.note("self._current is %s" % self._current)
        bb.note("patches is %s" % self.patches)
        if all:
            for i in self.patches:
                bb.note("applying patch %s" % i)
                self._applypatch(i, force)
                self._current = i
        else:
            if self._current is not None:
                next = self._current + 1
            else:
                next = 0

            bb.note("applying patch %s" % self.patches[next])
            ret = self._applypatch(self.patches[next], force)

            self._current = next
            return ret

    def Pop(self, force = None, all = None):
        if all:
            self._removePatchFile(True)
            self._current = None
        else:
            self._removePatchFile(False)

        if self._current == 0:
            self._current = None

        if self._current is not None:
            self._current = self._current - 1

    def Clean(self):
        """"""
        self.Pop(all=True)

class GitApplyTree(PatchTree):
    def __init__(self, dir, d):
        PatchTree.__init__(self, dir, d)

    def _applypatch(self, patch, force = False, reverse = False, run = True):
        def _applypatchhelper(shellcmd, patch, force = False, reverse = False, run = True):
            if reverse:
                shellcmd.append('-R')

            shellcmd.append(patch['file'])

            if not run:
                return "sh" + "-c" + " ".join(shellcmd)

            return runcmd(["sh", "-c", " ".join(shellcmd)], self.dir)

        try:
            shellcmd = ["git", "--work-tree=.", "am", "-3", "-p%s" % patch['strippath']]
            return _applypatchhelper(shellcmd, patch, force, reverse, run)
        except CmdError:
            shellcmd = ["git", "--git-dir=.", "apply", "-p%s" % patch['strippath']]
            return _applypatchhelper(shellcmd, patch, force, reverse, run)


class QuiltTree(PatchSet):
    def _runcmd(self, args, run = True):
        quiltrc = self.d.getVar('QUILTRCFILE', True)
        if not run:
            return ["quilt"] + ["--quiltrc"] + [quiltrc] + args
        runcmd(["quilt"] + ["--quiltrc"] + [quiltrc] + args, self.dir)

    def _quiltpatchpath(self, file):
        return os.path.join(self.dir, "patches", os.path.basename(file))


    def __init__(self, dir, d):
        PatchSet.__init__(self, dir, d)
        self.initialized = False
        p = os.path.join(self.dir, 'patches')
        if not os.path.exists(p):
            os.makedirs(p)

    def Clean(self):
        try:
            self._runcmd(["pop", "-a", "-f"])
            oe.path.remove(os.path.join(self.dir, "patches","series"))
        except Exception:
            pass
        self.initialized = True

    def InitFromDir(self):
        # read series -> self.patches
        seriespath = os.path.join(self.dir, 'patches', 'series')
        if not os.path.exists(self.dir):
            raise NotFoundError(self.dir)
        if os.path.exists(seriespath):
            series = file(seriespath, 'r')
            for line in series.readlines():
                patch = {}
                parts = line.strip().split()
                patch["quiltfile"] = self._quiltpatchpath(parts[0])
                patch["quiltfilemd5"] = bb.utils.md5_file(patch["quiltfile"])
                if len(parts) > 1:
                    patch["strippath"] = parts[1][2:]
                self.patches.append(patch)
            series.close()

            # determine which patches are applied -> self._current
            try:
                output = runcmd(["quilt", "applied"], self.dir)
            except CmdError:
                import sys
                if sys.exc_value.output.strip() == "No patches applied":
                    return
                else:
                    raise
            output = [val for val in output.split('\n') if not val.startswith('#')]
            for patch in self.patches:
                if os.path.basename(patch["quiltfile"]) == output[-1]:
                    self._current = self.patches.index(patch)
        self.initialized = True

    def Import(self, patch, force = None):
        if not self.initialized:
            self.InitFromDir()
        PatchSet.Import(self, patch, force)
        oe.path.symlink(patch["file"], self._quiltpatchpath(patch["file"]), force=True)
        f = open(os.path.join(self.dir, "patches","series"), "a");
        f.write(os.path.basename(patch["file"]) + " -p" + patch["strippath"]+"\n")
        f.close()
        patch["quiltfile"] = self._quiltpatchpath(patch["file"])
        patch["quiltfilemd5"] = bb.utils.md5_file(patch["quiltfile"])

        # TODO: determine if the file being imported:
        #      1) is already imported, and is the same
        #      2) is already imported, but differs

        self.patches.insert(self._current or 0, patch)


    def Push(self, force = False, all = False, run = True):
        # quilt push [-f]

        args = ["push"]
        if force:
            args.append("-f")
        if all:
            args.append("-a")
        if not run:
            return self._runcmd(args, run)

        self._runcmd(args)

        if self._current is not None:
            self._current = self._current + 1
        else:
            self._current = 0

    def Pop(self, force = None, all = None):
        # quilt pop [-f]
        args = ["pop"]
        if force:
            args.append("-f")
        if all:
            args.append("-a")

        self._runcmd(args)

        if self._current == 0:
            self._current = None

        if self._current is not None:
            self._current = self._current - 1

    def Refresh(self, **kwargs):
        if kwargs.get("remote"):
            patch = self.patches[kwargs["patch"]]
            if not patch:
                raise PatchError("No patch found at index %s in patchset." % kwargs["patch"])
            (type, host, path, user, pswd, parm) = bb.fetch.decodeurl(patch["remote"])
            if type == "file":
                import shutil
                if not patch.get("file") and patch.get("remote"):
                    patch["file"] = bb.fetch2.localpath(patch["remote"], self.d)

                shutil.copyfile(patch["quiltfile"], patch["file"])
            else:
                raise PatchError("Unable to do a remote refresh of %s, unsupported remote url scheme %s." % (os.path.basename(patch["quiltfile"]), type))
        else:
            # quilt refresh
            args = ["refresh"]
            if kwargs.get("quiltfile"):
                args.append(os.path.basename(kwargs["quiltfile"]))
            elif kwargs.get("patch"):
                args.append(os.path.basename(self.patches[kwargs["patch"]]["quiltfile"]))
            self._runcmd(args)

class Resolver(object):
    def __init__(self, patchset, terminal):
        raise NotImplementedError()

    def Resolve(self):
        raise NotImplementedError()

    def Revert(self):
        raise NotImplementedError()

    def Finalize(self):
        raise NotImplementedError()

class NOOPResolver(Resolver):
    def __init__(self, patchset, terminal):
        self.patchset = patchset
        self.terminal = terminal

    def Resolve(self):
        olddir = os.path.abspath(os.curdir)
        os.chdir(self.patchset.dir)
        try:
            self.patchset.Push()
        except Exception:
            import sys
            os.chdir(olddir)
            raise

# Patch resolver which relies on the user doing all the work involved in the
# resolution, with the exception of refreshing the remote copy of the patch
# files (the urls).
class UserResolver(Resolver):
    def __init__(self, patchset, terminal):
        self.patchset = patchset
        self.terminal = terminal

    # Force a push in the patchset, then drop to a shell for the user to
    # resolve any rejected hunks
    def Resolve(self):
        olddir = os.path.abspath(os.curdir)
        os.chdir(self.patchset.dir)
        try:
            self.patchset.Push(False)
        except CmdError as v:
            # Patch application failed
            patchcmd = self.patchset.Push(True, False, False)

            t = self.patchset.d.getVar('T', True)
            if not t:
                bb.msg.fatal("Build", "T not set")
            bb.utils.mkdirhier(t)
            import random
            rcfile = "%s/bashrc.%s.%s" % (t, str(os.getpid()), random.random())
            f = open(rcfile, "w")
            f.write("echo '*** Manual patch resolution mode ***'\n")
            f.write("echo 'Dropping to a shell, so patch rejects can be fixed manually.'\n")
            f.write("echo 'Run \"quilt refresh\" when patch is corrected, press CTRL+D to exit.'\n")
            f.write("echo ''\n")
            f.write(" ".join(patchcmd) + "\n")
            f.close()
            os.chmod(rcfile, 0775)

            self.terminal("bash --rcfile " + rcfile, 'Patch Rejects: Please fix patch rejects manually', self.patchset.d)

            # Construct a new PatchSet after the user's changes, compare the
            # sets, checking patches for modifications, and doing a remote
            # refresh on each.
            oldpatchset = self.patchset
            self.patchset = oldpatchset.__class__(self.patchset.dir, self.patchset.d)

            for patch in self.patchset.patches:
                oldpatch = None
                for opatch in oldpatchset.patches:
                    if opatch["quiltfile"] == patch["quiltfile"]:
                        oldpatch = opatch

                if oldpatch:
                    patch["remote"] = oldpatch["remote"]
                    if patch["quiltfile"] == oldpatch["quiltfile"]:
                        if patch["quiltfilemd5"] != oldpatch["quiltfilemd5"]:
                            bb.note("Patch %s has changed, updating remote url %s" % (os.path.basename(patch["quiltfile"]), patch["remote"]))
                            # user change?  remote refresh
                            self.patchset.Refresh(remote=True, patch=self.patchset.patches.index(patch))
                        else:
                            # User did not fix the problem.  Abort.
                            raise PatchError("Patch application failed, and user did not fix and refresh the patch.")
        except Exception:
            os.chdir(olddir)
            raise
        os.chdir(olddir)
