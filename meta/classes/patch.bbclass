# Copyright (C) 2006  OpenedHand LTD

def patch_init(d):
	import os, sys

	def md5sum(fname):
		import md5, sys

		f = file(fname, 'rb')
		m = md5.new()
		while True:
			d = f.read(8096)
			if not d:
				break
			m.update(d)
		f.close()
		return m.hexdigest()

	class CmdError(Exception):
		def __init__(self, exitstatus, output):
			self.status = exitstatus
			self.output = output

		def __str__(self):
			return "Command Error: exit status: %d  Output:\n%s" % (self.status, self.output)

	class NotFoundError(Exception):
		def __init__(self, path):
			self.path = path
		def __str__(self):
			return "Error: %s not found." % self.path

	def runcmd(args, dir = None):
		import commands

		if dir:
			olddir = os.path.abspath(os.curdir)
			if not os.path.exists(dir):
				raise NotFoundError(dir)
			os.chdir(dir)
			# print("cwd: %s -> %s" % (olddir, self.dir))

		try:
			args = [ commands.mkarg(str(arg)) for arg in args ]
			cmd = " ".join(args)
			# print("cmd: %s" % cmd)
			(exitstatus, output) = commands.getstatusoutput(cmd)
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

	import bb, bb.data, bb.fetch

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
					patch["file"] = bb.fetch.localpath(patch["remote"], self.d)

			for param in PatchSet.defaults:
				if not patch.get(param):
					patch[param] = PatchSet.defaults[param]

			if patch.get("remote"):
				patch["file"] = bb.data.expand(bb.fetch.localpath(patch["remote"], self.d), self.d)

			patch["filemd5"] = md5sum(patch["file"])

		def Push(self, force):
			raise NotImplementedError()

		def Pop(self, force):
			raise NotImplementedError()

		def Refresh(self, remote = None, all = None):
			raise NotImplementedError()


	class PatchTree(PatchSet):
		def __init__(self, dir, d):
			PatchSet.__init__(self, dir, d)

		def Import(self, patch, force = None):
			""""""
			PatchSet.Import(self, patch, force)

			self.patches.insert(self._current or 0, patch)

		def _applypatch(self, patch, force = None, reverse = None):
			shellcmd = ["patch", "<", patch['file'], "-p", patch['strippath']]
			if reverse:
				shellcmd.append('-R')
			shellcmd.append('--dry-run')

			try:
				output = runcmd(["sh", "-c", " ".join(shellcmd)], self.dir)
			except CmdError:
				if force:
					shellcmd.pop(len(shellcmd) - 1)
					output = runcmd(["sh", "-c", " ".join(shellcmd)], self.dir)
				else:
					import sys
					raise sys.exc_value

			return output

		def Push(self, force = None, all = None):
			if all:
				for i in self.patches:
					if self._current is not None:
						self._current = self._current + 1
					else:
						self._current = 0
					self._applypatch(i, force)
			else:
				if self._current is not None:
					self._current = self._current + 1
				else:
					self._current = 0
				self._applypatch(self.patches[self._current], force)


		def Pop(self, force = None, all = None):
			if all:
				for i in self.patches:
					self._applypatch(i, force, True)
			else:
				self._applypatch(self.patches[self._current], force, True)

		def Clean(self):
			""""""

	class QuiltTree(PatchSet):
		def _runcmd(self, args):
			runcmd(["quilt"] + args, self.dir)

		def _quiltpatchpath(self, file):
			return os.path.join(self.dir, "patches", os.path.basename(file))


		def __init__(self, dir, d):
			PatchSet.__init__(self, dir, d)
			self.initialized = False

		def Clean(self):
			try:
				self._runcmd(["pop", "-a", "-f"])
			except CmdError:
				pass
			except NotFoundError:
				pass
			# runcmd(["rm", "-rf", os.path.join(self.dir, "patches"), os.path.join(self.dir, ".pc")])
			self.initialized = True

		def InitFromDir(self):
			# read series -> self.patches
			seriespath = os.path.join(self.dir, 'patches', 'series')
			if not os.path.exists(self.dir):
				raise Exception("Error: %s does not exist." % self.dir)
			if os.path.exists(seriespath):
				series = file(seriespath, 'r')
				for line in series.readlines():
					patch = {}
					parts = line.strip().split()
					patch["quiltfile"] = self._quiltpatchpath(parts[0])
					patch["quiltfilemd5"] = md5sum(patch["quiltfile"])
					if len(parts) > 1:
						patch["strippath"] = parts[1][2:]
					self.patches.append(patch)
				series.close()

				# determine which patches are applied -> self._current
				try:
					output = runcmd(["quilt", "applied"], self.dir)
				except CmdError:
					if sys.exc_value.output.strip() == "No patches applied":
						return
					else:
						raise sys.exc_value
				output = [val for val in output.split('\n') if not val.startswith('#')]
				for patch in self.patches:
					if os.path.basename(patch["quiltfile"]) == output[-1]:
						self._current = self.patches.index(patch)
			self.initialized = True

		def Import(self, patch, force = None):
			if not self.initialized:
				self.InitFromDir()
			PatchSet.Import(self, patch, force)

			args = ["import", "-p", patch["strippath"]]
			if force:
				args.append("-f")
			args.append(patch["file"])

			self._runcmd(args)

			patch["quiltfile"] = self._quiltpatchpath(patch["file"])
			patch["quiltfilemd5"] = md5sum(patch["quiltfile"])

			# TODO: determine if the file being imported:
			#	   1) is already imported, and is the same
			#	   2) is already imported, but differs

			self.patches.insert(self._current or 0, patch)


		def Push(self, force = None, all = None):
			# quilt push [-f]

			args = ["push"]
			if force:
				args.append("-f")
			if all:
				args.append("-a")

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
				(type, host, path, user, pswd, parm) = bb.decodeurl(patch["remote"])
				if type == "file":
					import shutil
					if not patch.get("file") and patch.get("remote"):
						patch["file"] = bb.fetch.localpath(patch["remote"], self.d)

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
		def __init__(self, patchset):
			raise NotImplementedError()

		def Resolve(self):
			raise NotImplementedError()

		def Revert(self):
			raise NotImplementedError()

		def Finalize(self):
			raise NotImplementedError()

	# Patch resolver which relies on the user doing all the work involved in the
	# resolution, with the exception of refreshing the remote copy of the patch
	# files (the urls).
	class UserResolver(Resolver):
		def __init__(self, patchset):
			self.patchset = patchset

		# Force a push in the patchset, then drop to a shell for the user to
		# resolve any rejected hunks
		def Resolve(self):

			olddir = os.path.abspath(os.curdir)
			os.chdir(self.patchset.dir)
			try:
				self.patchset.Push(True)
			except CmdError, v:
				# Patch application failed
				if sys.exc_value.output.strip() == "No patches applied":
					return
				print(sys.exc_value)
				print('NOTE: dropping user into a shell, so that patch rejects can be fixed manually.')

				os.system('/bin/sh')

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

		# Throw away the changes to the patches in the patchset made by resolve()
		def Revert(self):
			raise NotImplementedError()

		# Apply the changes to the patches in the patchset made by resolve()
		def Finalize(self):
			raise NotImplementedError()

	g = globals()
	g["PatchSet"] = PatchSet
	g["PatchTree"] = PatchTree
	g["QuiltTree"] = QuiltTree
	g["Resolver"] = Resolver
	g["UserResolver"] = UserResolver
	g["NotFoundError"] = NotFoundError
	g["CmdError"] = CmdError

addtask patch after do_unpack
do_patch[dirs] = "${WORKDIR}"
python patch_do_patch() {
	import re
	import bb.fetch

	patch_init(d)

	src_uri = (bb.data.getVar('SRC_URI', d, 1) or '').split()
	if not src_uri:
		return

	patchsetmap = {
		"patch": PatchTree,
		"quilt": QuiltTree,
	}

	cls = patchsetmap[bb.data.getVar('PATCHTOOL', d, 1) or 'quilt']

	resolvermap = {
		"user": UserResolver,
	}

	rcls = resolvermap[bb.data.getVar('PATCHRESOLVE', d, 1) or 'user']

	s = bb.data.getVar('S', d, 1)

	path = os.getenv('PATH')
	os.putenv('PATH', bb.data.getVar('PATH', d, 1))
	patchset = cls(s, d)
	patchset.Clean()

	resolver = rcls(patchset)

	workdir = bb.data.getVar('WORKDIR', d, 1)
	for url in src_uri:
		(type, host, path, user, pswd, parm) = bb.decodeurl(url)
		if not "patch" in parm:
			continue

		bb.fetch.init([url],d)
		url = bb.encodeurl((type, host, path, user, pswd, []))
		local = os.path.join('/', bb.fetch.localpath(url, d))

		# did it need to be unpacked?
		dots = os.path.basename(local).split(".")
		if dots[-1] in ['gz', 'bz2', 'Z']:
			unpacked = os.path.join(bb.data.getVar('WORKDIR', d),'.'.join(dots[0:-1]))
		else:
			unpacked = local
		unpacked = bb.data.expand(unpacked, d)

		if "pnum" in parm:
			pnum = parm["pnum"]
		else:
			pnum = "1"

		if "pname" in parm:
			pname = parm["pname"]
		else:
			pname = os.path.basename(unpacked)

		if "mindate" in parm:
			mindate = parm["mindate"]
		else:
			mindate = 0

		if "maxdate" in parm:
			maxdate = parm["maxdate"]
		else:
			maxdate = "20711226"

		pn = bb.data.getVar('PN', d, 1)
		srcdate = bb.data.getVar('SRCDATE_%s' % pn, d, 1)

		if not srcdate:
			srcdate = bb.data.getVar('SRCDATE', d, 1)

		if srcdate == "now":
			srcdate = bb.data.getVar('DATE', d, 1)

		if (maxdate < srcdate) or (mindate > srcdate):
			if (maxdate < srcdate):
				bb.note("Patch '%s' is outdated" % pname)

			if (mindate > srcdate):
				bb.note("Patch '%s' is predated" % pname)

			continue

		bb.note("Applying patch '%s'" % pname)
		try:
			patchset.Import({"file":unpacked, "remote":url, "strippath": pnum}, True)
		except NotFoundError:
			import sys
			raise bb.build.FuncFailed(str(sys.exc_value))
		resolver.Resolve()
}

EXPORT_FUNCTIONS do_patch
