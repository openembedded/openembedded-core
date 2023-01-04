#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: GPL-2.0-only
#

import os
import glob
import stat
import mmap
import subprocess

def runstrip(arg):
    # Function to strip a single file, called from split_and_strip_files below
    # A working 'file' (one which works on the target architecture)
    #
    # The elftype is a bit pattern (explained in is_elf below) to tell
    # us what type of file we're processing...
    # 4 - executable
    # 8 - shared library
    # 16 - kernel module

    if len(arg) == 3:
        (file, elftype, strip) = arg
        extra_strip_sections = ''
    else:
        (file, elftype, strip, extra_strip_sections) = arg

    newmode = None
    if not os.access(file, os.W_OK) or os.access(file, os.R_OK):
        origmode = os.stat(file)[stat.ST_MODE]
        newmode = origmode | stat.S_IWRITE | stat.S_IREAD
        os.chmod(file, newmode)

    stripcmd = [strip]
    skip_strip = False
    # kernel module    
    if elftype & 16:
        if is_kernel_module_signed(file):
            bb.debug(1, "Skip strip on signed module %s" % file)
            skip_strip = True
        else:
            stripcmd.extend(["--strip-debug", "--remove-section=.comment",
                "--remove-section=.note", "--preserve-dates"])
    # .so and shared library
    elif ".so" in file and elftype & 8:
        stripcmd.extend(["--remove-section=.comment", "--remove-section=.note", "--strip-unneeded"])
    # shared or executable:
    elif elftype & 8 or elftype & 4:
        stripcmd.extend(["--remove-section=.comment", "--remove-section=.note"])
        if extra_strip_sections != '':
            for section in extra_strip_sections.split():
                stripcmd.extend(["--remove-section=" + section])

    stripcmd.append(file)
    bb.debug(1, "runstrip: %s" % stripcmd)

    if not skip_strip:
        output = subprocess.check_output(stripcmd, stderr=subprocess.STDOUT)

    if newmode:
        os.chmod(file, origmode)

# Detect .ko module by searching for "vermagic=" string
def is_kernel_module(path):
    with open(path) as f:
        return mmap.mmap(f.fileno(), 0, prot=mmap.PROT_READ).find(b"vermagic=") >= 0

# Detect if .ko module is signed
def is_kernel_module_signed(path):
    with open(path, "rb") as f:
        f.seek(-28, 2)
        module_tail = f.read()
        return "Module signature appended" in "".join(chr(c) for c in bytearray(module_tail))

# Return type (bits):
# 0 - not elf
# 1 - ELF
# 2 - stripped
# 4 - executable
# 8 - shared library
# 16 - kernel module
def is_elf(path):
    exec_type = 0
    result = subprocess.check_output(["file", "-b", path], stderr=subprocess.STDOUT).decode("utf-8")

    if "ELF" in result:
        exec_type |= 1
        if "not stripped" not in result:
            exec_type |= 2
        if "executable" in result:
            exec_type |= 4
        if "shared" in result:
            exec_type |= 8
        if "relocatable" in result:
            if path.endswith(".ko") and path.find("/lib/modules/") != -1 and is_kernel_module(path):
                exec_type |= 16
    return (path, exec_type)

def is_static_lib(path):
    if path.endswith('.a') and not os.path.islink(path):
        with open(path, 'rb') as fh:
            # The magic must include the first slash to avoid
            # matching golang static libraries
            magic = b'!<arch>\x0a/'
            start = fh.read(len(magic))
            return start == magic
    return False

def strip_execs(pn, dstdir, strip_cmd, libdir, base_libdir, d, qa_already_stripped=False):
    """
    Strip executable code (like executables, shared libraries) _in_place_
    - Based on sysroot_strip in staging.bbclass
    :param dstdir: directory in which to strip files
    :param strip_cmd: Strip command (usually ${STRIP})
    :param libdir: ${libdir} - strip .so files in this directory
    :param base_libdir: ${base_libdir} - strip .so files in this directory
    :param qa_already_stripped: Set to True if already-stripped' in ${INSANE_SKIP}
    This is for proper logging and messages only.
    """
    import stat, errno, oe.path, oe.utils

    elffiles = {}
    inodes = {}
    libdir = os.path.abspath(dstdir + os.sep + libdir)
    base_libdir = os.path.abspath(dstdir + os.sep + base_libdir)
    exec_mask = stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH
    #
    # First lets figure out all of the files we may have to process
    #
    checkelf = []
    inodecache = {}
    for root, dirs, files in os.walk(dstdir):
        for f in files:
            file = os.path.join(root, f)

            try:
                ltarget = oe.path.realpath(file, dstdir, False)
                s = os.lstat(ltarget)
            except OSError as e:
                (err, strerror) = e.args
                if err != errno.ENOENT:
                    raise
                # Skip broken symlinks
                continue
            if not s:
                continue
            # Check its an excutable
            if s[stat.ST_MODE] & exec_mask \
                    or ((file.startswith(libdir) or file.startswith(base_libdir)) and ".so" in f) \
                    or file.endswith('.ko'):
                # If it's a symlink, and points to an ELF file, we capture the readlink target
                if os.path.islink(file):
                    continue

                # It's a file (or hardlink), not a link
                # ...but is it ELF, and is it already stripped?
                checkelf.append(file)
                inodecache[file] = s.st_ino
    results = oe.utils.multiprocess_launch(is_elf, checkelf, d)
    for (file, elf_file) in results:
                #elf_file = is_elf(file)
                if elf_file & 1:
                    if elf_file & 2:
                        if qa_already_stripped:
                            bb.note("Skipping file %s from %s for already-stripped QA test" % (file[len(dstdir):], pn))
                        else:
                            bb.warn("File '%s' from %s was already stripped, this will prevent future debugging!" % (file[len(dstdir):], pn))
                        continue

                    if inodecache[file] in inodes:
                        os.unlink(file)
                        os.link(inodes[inodecache[file]], file)
                    else:
                        # break hardlinks so that we do not strip the original.
                        inodes[inodecache[file]] = file
                        bb.utils.break_hardlinks(file)
                        elffiles[file] = elf_file

    #
    # Now strip them (in parallel)
    #
    sfiles = []
    for file in elffiles:
        elf_file = int(elffiles[file])
        sfiles.append((file, elf_file, strip_cmd))

    oe.utils.multiprocess_launch(runstrip, sfiles, d)


def file_translate(file):
    ft = file.replace("@", "@at@")
    ft = ft.replace(" ", "@space@")
    ft = ft.replace("\t", "@tab@")
    ft = ft.replace("[", "@openbrace@")
    ft = ft.replace("]", "@closebrace@")
    ft = ft.replace("_", "@underscore@")
    return ft

def filedeprunner(arg):
    import re, subprocess, shlex

    (pkg, pkgfiles, rpmdeps, pkgdest) = arg
    provides = {}
    requires = {}

    file_re = re.compile(r'\s+\d+\s(.*)')
    dep_re = re.compile(r'\s+(\S)\s+(.*)')
    r = re.compile(r'[<>=]+\s+\S*')

    def process_deps(pipe, pkg, pkgdest, provides, requires):
        file = None
        for line in pipe.split("\n"):

            m = file_re.match(line)
            if m:
                file = m.group(1)
                file = file.replace(pkgdest + "/" + pkg, "")
                file = file_translate(file)
                continue

            m = dep_re.match(line)
            if not m or not file:
                continue

            type, dep = m.groups()

            if type == 'R':
                i = requires
            elif type == 'P':
                i = provides
            else:
               continue

            if dep.startswith("python("):
                continue

            # Ignore all perl(VMS::...) and perl(Mac::...) dependencies. These
            # are typically used conditionally from the Perl code, but are
            # generated as unconditional dependencies.
            if dep.startswith('perl(VMS::') or dep.startswith('perl(Mac::'):
                continue

            # Ignore perl dependencies on .pl files.
            if dep.startswith('perl(') and dep.endswith('.pl)'):
                continue

            # Remove perl versions and perl module versions since they typically
            # do not make sense when used as package versions.
            if dep.startswith('perl') and r.search(dep):
                dep = dep.split()[0]

            # Put parentheses around any version specifications.
            dep = r.sub(r'(\g<0>)',dep)

            if file not in i:
                i[file] = []
            i[file].append(dep)

        return provides, requires

    output = subprocess.check_output(shlex.split(rpmdeps) + pkgfiles, stderr=subprocess.STDOUT).decode("utf-8")
    provides, requires = process_deps(output, pkg, pkgdest, provides, requires)

    return (pkg, provides, requires)


def read_shlib_providers(d):
    import re

    shlib_provider = {}
    shlibs_dirs = d.getVar('SHLIBSDIRS').split()
    list_re = re.compile(r'^(.*)\.list$')
    # Go from least to most specific since the last one found wins
    for dir in reversed(shlibs_dirs):
        bb.debug(2, "Reading shlib providers in %s" % (dir))
        if not os.path.exists(dir):
            continue
        for file in sorted(os.listdir(dir)):
            m = list_re.match(file)
            if m:
                dep_pkg = m.group(1)
                try:
                    fd = open(os.path.join(dir, file))
                except IOError:
                    # During a build unrelated shlib files may be deleted, so
                    # handle files disappearing between the listdirs and open.
                    continue
                lines = fd.readlines()
                fd.close()
                for l in lines:
                    s = l.strip().split(":")
                    if s[0] not in shlib_provider:
                        shlib_provider[s[0]] = {}
                    shlib_provider[s[0]][s[1]] = (dep_pkg, s[2])
    return shlib_provider

# We generate a master list of directories to process, we start by
# seeding this list with reasonable defaults, then load from
# the fs-perms.txt files
def fixup_perms(d):
    import pwd, grp
    import oe.cachedpath

    cpath = oe.cachedpath.CachedPath()
    dvar = d.getVar('PKGD')

    # init using a string with the same format as a line as documented in
    # the fs-perms.txt file
    # <path> <mode> <uid> <gid> <walk> <fmode> <fuid> <fgid>
    # <path> link <link target>
    #
    # __str__ can be used to print out an entry in the input format
    #
    # if fs_perms_entry.path is None:
    #    an error occurred
    # if fs_perms_entry.link, you can retrieve:
    #    fs_perms_entry.path = path
    #    fs_perms_entry.link = target of link
    # if not fs_perms_entry.link, you can retrieve:
    #    fs_perms_entry.path = path
    #    fs_perms_entry.mode = expected dir mode or None
    #    fs_perms_entry.uid = expected uid or -1
    #    fs_perms_entry.gid = expected gid or -1
    #    fs_perms_entry.walk = 'true' or something else
    #    fs_perms_entry.fmode = expected file mode or None
    #    fs_perms_entry.fuid = expected file uid or -1
    #    fs_perms_entry_fgid = expected file gid or -1
    class fs_perms_entry():
        def __init__(self, line):
            lsplit = line.split()
            if len(lsplit) == 3 and lsplit[1].lower() == "link":
                self._setlink(lsplit[0], lsplit[2])
            elif len(lsplit) == 8:
                self._setdir(lsplit[0], lsplit[1], lsplit[2], lsplit[3], lsplit[4], lsplit[5], lsplit[6], lsplit[7])
            else:
                msg = "Fixup Perms: invalid config line %s" % line
                oe.qa.handle_error("perm-config", msg, d)
                self.path = None
                self.link = None

        def _setdir(self, path, mode, uid, gid, walk, fmode, fuid, fgid):
            self.path = os.path.normpath(path)
            self.link = None
            self.mode = self._procmode(mode)
            self.uid  = self._procuid(uid)
            self.gid  = self._procgid(gid)
            self.walk = walk.lower()
            self.fmode = self._procmode(fmode)
            self.fuid = self._procuid(fuid)
            self.fgid = self._procgid(fgid)

        def _setlink(self, path, link):
            self.path = os.path.normpath(path)
            self.link = link

        def _procmode(self, mode):
            if not mode or (mode and mode == "-"):
                return None
            else:
                return int(mode,8)

        # Note uid/gid -1 has special significance in os.lchown
        def _procuid(self, uid):
            if uid is None or uid == "-":
                return -1
            elif uid.isdigit():
                return int(uid)
            else:
                return pwd.getpwnam(uid).pw_uid

        def _procgid(self, gid):
            if gid is None or gid == "-":
                return -1
            elif gid.isdigit():
                return int(gid)
            else:
                return grp.getgrnam(gid).gr_gid

        # Use for debugging the entries
        def __str__(self):
            if self.link:
                return "%s link %s" % (self.path, self.link)
            else:
                mode = "-"
                if self.mode:
                    mode = "0%o" % self.mode
                fmode = "-"
                if self.fmode:
                    fmode = "0%o" % self.fmode
                uid = self._mapugid(self.uid)
                gid = self._mapugid(self.gid)
                fuid = self._mapugid(self.fuid)
                fgid = self._mapugid(self.fgid)
                return "%s %s %s %s %s %s %s %s" % (self.path, mode, uid, gid, self.walk, fmode, fuid, fgid)

        def _mapugid(self, id):
            if id is None or id == -1:
                return "-"
            else:
                return "%d" % id

    # Fix the permission, owner and group of path
    def fix_perms(path, mode, uid, gid, dir):
        if mode and not os.path.islink(path):
            #bb.note("Fixup Perms: chmod 0%o %s" % (mode, dir))
            os.chmod(path, mode)
        # -1 is a special value that means don't change the uid/gid
        # if they are BOTH -1, don't bother to lchown
        if not (uid == -1 and gid == -1):
            #bb.note("Fixup Perms: lchown %d:%d %s" % (uid, gid, dir))
            os.lchown(path, uid, gid)

    # Return a list of configuration files based on either the default
    # files/fs-perms.txt or the contents of FILESYSTEM_PERMS_TABLES
    # paths are resolved via BBPATH
    def get_fs_perms_list(d):
        str = ""
        bbpath = d.getVar('BBPATH')
        fs_perms_tables = d.getVar('FILESYSTEM_PERMS_TABLES') or ""
        for conf_file in fs_perms_tables.split():
            confpath = bb.utils.which(bbpath, conf_file)
            if confpath:
                str += " %s" % bb.utils.which(bbpath, conf_file)
            else:
                bb.warn("cannot find %s specified in FILESYSTEM_PERMS_TABLES" % conf_file)
        return str

    fs_perms_table = {}
    fs_link_table = {}

    # By default all of the standard directories specified in
    # bitbake.conf will get 0755 root:root.
    target_path_vars = [    'base_prefix',
                'prefix',
                'exec_prefix',
                'base_bindir',
                'base_sbindir',
                'base_libdir',
                'datadir',
                'sysconfdir',
                'servicedir',
                'sharedstatedir',
                'localstatedir',
                'infodir',
                'mandir',
                'docdir',
                'bindir',
                'sbindir',
                'libexecdir',
                'libdir',
                'includedir',
                'oldincludedir' ]

    for path in target_path_vars:
        dir = d.getVar(path) or ""
        if dir == "":
            continue
        fs_perms_table[dir] = fs_perms_entry(d.expand("%s 0755 root root false - - -" % (dir)))

    # Now we actually load from the configuration files
    for conf in get_fs_perms_list(d).split():
        if not os.path.exists(conf):
            continue
        with open(conf) as f:
            for line in f:
                if line.startswith('#'):
                    continue
                lsplit = line.split()
                if len(lsplit) == 0:
                    continue
                if len(lsplit) != 8 and not (len(lsplit) == 3 and lsplit[1].lower() == "link"):
                    msg = "Fixup perms: %s invalid line: %s" % (conf, line)
                    oe.qa.handle_error("perm-line", msg, d)
                    continue
                entry = fs_perms_entry(d.expand(line))
                if entry and entry.path:
                    if entry.link:
                        fs_link_table[entry.path] = entry
                        if entry.path in fs_perms_table:
                            fs_perms_table.pop(entry.path)
                    else:
                        fs_perms_table[entry.path] = entry
                        if entry.path in fs_link_table:
                            fs_link_table.pop(entry.path)

    # Debug -- list out in-memory table
    #for dir in fs_perms_table:
    #    bb.note("Fixup Perms: %s: %s" % (dir, str(fs_perms_table[dir])))
    #for link in fs_link_table:
    #    bb.note("Fixup Perms: %s: %s" % (link, str(fs_link_table[link])))

    # We process links first, so we can go back and fixup directory ownership
    # for any newly created directories
    # Process in sorted order so /run gets created before /run/lock, etc.
    for entry in sorted(fs_link_table.values(), key=lambda x: x.link):
        link = entry.link
        dir = entry.path
        origin = dvar + dir
        if not (cpath.exists(origin) and cpath.isdir(origin) and not cpath.islink(origin)):
            continue

        if link[0] == "/":
            target = dvar + link
            ptarget = link
        else:
            target = os.path.join(os.path.dirname(origin), link)
            ptarget = os.path.join(os.path.dirname(dir), link)
        if os.path.exists(target):
            msg = "Fixup Perms: Unable to correct directory link, target already exists: %s -> %s" % (dir, ptarget)
            oe.qa.handle_error("perm-link", msg, d)
            continue

        # Create path to move directory to, move it, and then setup the symlink
        bb.utils.mkdirhier(os.path.dirname(target))
        #bb.note("Fixup Perms: Rename %s -> %s" % (dir, ptarget))
        bb.utils.rename(origin, target)
        #bb.note("Fixup Perms: Link %s -> %s" % (dir, link))
        os.symlink(link, origin)

    for dir in fs_perms_table:
        origin = dvar + dir
        if not (cpath.exists(origin) and cpath.isdir(origin)):
            continue

        fix_perms(origin, fs_perms_table[dir].mode, fs_perms_table[dir].uid, fs_perms_table[dir].gid, dir)

        if fs_perms_table[dir].walk == 'true':
            for root, dirs, files in os.walk(origin):
                for dr in dirs:
                    each_dir = os.path.join(root, dr)
                    fix_perms(each_dir, fs_perms_table[dir].mode, fs_perms_table[dir].uid, fs_perms_table[dir].gid, dir)
                for f in files:
                    each_file = os.path.join(root, f)
                    fix_perms(each_file, fs_perms_table[dir].fmode, fs_perms_table[dir].fuid, fs_perms_table[dir].fgid, dir)

# Get a list of files from file vars by searching files under current working directory
# The list contains symlinks, directories and normal files.
def files_from_filevars(filevars):
    import oe.cachedpath

    cpath = oe.cachedpath.CachedPath()
    files = []
    for f in filevars:
        if os.path.isabs(f):
            f = '.' + f
        if not f.startswith("./"):
            f = './' + f
        globbed = glob.glob(f)
        if globbed:
            if [ f ] != globbed:
                files += globbed
                continue
        files.append(f)

    symlink_paths = []
    for ind, f in enumerate(files):
        # Handle directory symlinks. Truncate path to the lowest level symlink
        parent = ''
        for dirname in f.split('/')[:-1]:
            parent = os.path.join(parent, dirname)
            if dirname == '.':
                continue
            if cpath.islink(parent):
                bb.warn("FILES contains file '%s' which resides under a "
                        "directory symlink. Please fix the recipe and use the "
                        "real path for the file." % f[1:])
                symlink_paths.append(f)
                files[ind] = parent
                f = parent
                break

        if not cpath.islink(f):
            if cpath.isdir(f):
                newfiles = [ os.path.join(f,x) for x in os.listdir(f) ]
                if newfiles:
                    files += newfiles

    return files, symlink_paths

# Called in package_<rpm,ipk,deb>.bbclass to get the correct list of configuration files
def get_conffiles(pkg, d):
    pkgdest = d.getVar('PKGDEST')
    root = os.path.join(pkgdest, pkg)
    cwd = os.getcwd()
    os.chdir(root)

    conffiles = d.getVar('CONFFILES:%s' % pkg);
    if conffiles == None:
        conffiles = d.getVar('CONFFILES')
    if conffiles == None:
        conffiles = ""
    conffiles = conffiles.split()
    conf_orig_list = files_from_filevars(conffiles)[0]

    # Remove links and directories from conf_orig_list to get conf_list which only contains normal files
    conf_list = []
    for f in conf_orig_list:
        if os.path.isdir(f):
            continue
        if os.path.islink(f):
            continue
        if not os.path.exists(f):
            continue
        conf_list.append(f)

    # Remove the leading './'
    for i in range(0, len(conf_list)):
        conf_list[i] = conf_list[i][1:]

    os.chdir(cwd)
    return conf_list

