#
# Packaging process
#
# Executive summary: This class iterates over the functions listed in PACKAGEFUNCS
# Taking D and spliting it up into the packages listed in PACKAGES, placing the 
# resulting output in PKGDEST.
#
# There are the following default steps but PACKAGEFUNCS can be extended:
#
# a) package_get_auto_pr - get PRAUTO from remote PR service
#
# b) perform_packagecopy - Copy D into PKGD
#
# c) package_do_split_locales - Split out the locale files, updates FILES and PACKAGES
#
# d) split_and_strip_files - split the files into runtime and debug and strip them.
#    Debug files include debug info split, and associated sources that end up in -dbg packages
#
# e) fixup_perms - Fix up permissions in the package before we split it.
#
# f) populate_packages - Split the files in PKGD into separate packages in PKGDEST/<pkgname>
#    Also triggers the binary stripping code to put files in -dbg packages.
#
# g) package_do_filedeps - Collect perfile run-time dependency metadata
#    The data is stores in FILER{PROVIDES,DEPENDS}_file_pkg variables with
#    a list of affected files in FILER{PROVIDES,DEPENDS}FLIST_pkg
#
# h) package_do_shlibs - Look at the shared libraries generated and autotmatically add any 
#    depenedencies found. Also stores the package name so anyone else using this library 
#    knows which package to depend on.
#
# i) package_do_pkgconfig - Keep track of which packages need and provide which .pc files
#
# j) read_shlibdeps - Reads the stored shlibs information into the metadata
#
# k) package_depchains - Adds automatic dependencies to -dbg and -dev packages
#
# l) emit_pkgdata - saves the packaging data into PKGDATA_DIR for use in later 
#    packaging steps

inherit packagedata
inherit prserv

PKGD    = "${WORKDIR}/package"
PKGDEST = "${WORKDIR}/packages-split"

LOCALE_SECTION ?= ''

ALL_MULTILIB_PACKAGE_ARCHS = "${@all_multilib_tune_values(d, 'PACKAGE_ARCHS')}"

# rpm is used for the per-file dependency identification
PACKAGE_DEPENDS += "rpm-native"

def legitimize_package_name(s):
	"""
	Make sure package names are legitimate strings
	"""
	import re

	def fixutf(m):
		cp = m.group(1)
		if cp:
			return ('\u%s' % cp).decode('unicode_escape').encode('utf-8')

	# Handle unicode codepoints encoded as <U0123>, as in glibc locale files.
	s = re.sub('<U([0-9A-Fa-f]{1,4})>', fixutf, s)

	# Remaining package name validity fixes
	return s.lower().replace('_', '-').replace('@', '+').replace(',', '+').replace('/', '-')

def do_split_packages(d, root, file_regex, output_pattern, description, postinst=None, recursive=False, hook=None, extra_depends=None, aux_files_pattern=None, postrm=None, allow_dirs=False, prepend=False, match_path=False, aux_files_pattern_verbatim=None, allow_links=False):
	"""
	Used in .bb files to split up dynamically generated subpackages of a 
	given package, usually plugins or modules.
	"""

	ml = d.getVar("MLPREFIX", True)
	if ml:
		if not output_pattern.startswith(ml):
			output_pattern = ml + output_pattern

		newdeps = []
		for dep in (extra_depends or "").split():
			if dep.startswith(ml):
				newdeps.append(dep)
			else:
				newdeps.append(ml + dep)
		if newdeps:
			extra_depends = " ".join(newdeps)

	dvar = bb.data.getVar('PKGD', d, True)

	packages = bb.data.getVar('PACKAGES', d, True).split()

	if postinst:
		postinst = '#!/bin/sh\n' + postinst + '\n'
	if postrm:
		postrm = '#!/bin/sh\n' + postrm + '\n'
	if not recursive:
		objs = os.listdir(dvar + root)
	else:
		objs = []
		for walkroot, dirs, files in os.walk(dvar + root):
			for file in files:
				relpath = os.path.join(walkroot, file).replace(dvar + root + '/', '', 1)
				if relpath:
					objs.append(relpath)

	if extra_depends == None:
		# This is *really* broken
		mainpkg = packages[0]
		# At least try and patch it up I guess...
		if mainpkg.find('-dbg'):
			mainpkg = mainpkg.replace('-dbg', '')
		if mainpkg.find('-dev'):
			mainpkg = mainpkg.replace('-dev', '')
		extra_depends = mainpkg

	for o in objs:
		import re, stat
		if match_path:
			m = re.match(file_regex, o)
		else:
			m = re.match(file_regex, os.path.basename(o))
		
		if not m:
			continue
		f = os.path.join(dvar + root, o)
		mode = os.lstat(f).st_mode
		if not (stat.S_ISREG(mode) or (allow_links and stat.S_ISLNK(mode)) or (allow_dirs and stat.S_ISDIR(mode))):
			continue
		on = legitimize_package_name(m.group(1))
		pkg = output_pattern % on
		if not pkg in packages:
			if prepend:
				packages = [pkg] + packages
			else:
				packages.append(pkg)
		oldfiles = bb.data.getVar('FILES_' + pkg, d, True)
		if not oldfiles:
			the_files = [os.path.join(root, o)]
			if aux_files_pattern:
				if type(aux_files_pattern) is list:
					for fp in aux_files_pattern:
						the_files.append(fp % on)	
				else:
					the_files.append(aux_files_pattern % on)
			if aux_files_pattern_verbatim:
				if type(aux_files_pattern_verbatim) is list:
					for fp in aux_files_pattern_verbatim:
						the_files.append(fp % m.group(1))	
				else:
					the_files.append(aux_files_pattern_verbatim % m.group(1))
			bb.data.setVar('FILES_' + pkg, " ".join(the_files), d)
			if extra_depends != '':
				the_depends = bb.data.getVar('RDEPENDS_' + pkg, d, True)
				if the_depends:
					the_depends = '%s %s' % (the_depends, extra_depends)
				else:
					the_depends = extra_depends
				bb.data.setVar('RDEPENDS_' + pkg, the_depends, d)
			bb.data.setVar('DESCRIPTION_' + pkg, description % on, d)
			if postinst:
				bb.data.setVar('pkg_postinst_' + pkg, postinst, d)
			if postrm:
				bb.data.setVar('pkg_postrm_' + pkg, postrm, d)
		else:
			bb.data.setVar('FILES_' + pkg, oldfiles + " " + os.path.join(root, o), d)
		if callable(hook):
			hook(f, pkg, file_regex, output_pattern, m.group(1))

	bb.data.setVar('PACKAGES', ' '.join(packages), d)

PACKAGE_DEPENDS += "file-native"

python () {
    if bb.data.getVar('PACKAGES', d, True) != '':
        deps = bb.data.getVarFlag('do_package', 'depends', d) or ""
        for dep in (bb.data.getVar('PACKAGE_DEPENDS', d, True) or "").split():
            deps += " %s:do_populate_sysroot" % dep
        bb.data.setVarFlag('do_package', 'depends', deps, d)

        deps = (bb.data.getVarFlag('do_package', 'deptask', d) or "").split()
        # shlibs requires any DEPENDS to have already packaged for the *.list files
        deps.append("do_package")
        bb.data.setVarFlag('do_package', 'deptask', " ".join(deps), d)
    elif not bb.data.inherits_class('image', d):
        d.setVar("PACKAGERDEPTASK", "")
}

def splitfile(file, debugfile, debugsrcdir, d):
    # Function to split a single file, called from split_and_strip_files below
    # A working 'file' (one which works on the target architecture)
    # is split and the split off portions go to debugfile.
    #
    # The debug information is then processed for src references.  These
    # references are copied to debugsrcdir, if defined.

    import commands, stat

    dvar = bb.data.getVar('PKGD', d, True)
    pathprefix = "export PATH=%s; " % bb.data.getVar('PATH', d, True)
    objcopy = bb.data.getVar("OBJCOPY", d, True)
    debugedit = bb.data.expand("${STAGING_LIBDIR_NATIVE}/rpm/bin/debugedit", d)
    workdir = bb.data.expand("${WORKDIR}", d)
    workparentdir = os.path.dirname(workdir)
    sourcefile = bb.data.expand("${WORKDIR}/debugsources.list", d)

    # We ignore kernel modules, we don't generate debug info files.
    if file.find("/lib/modules/") != -1 and file.endswith(".ko"):
	return 1

    newmode = None
    if not os.access(file, os.W_OK) or os.access(file, os.R_OK):
        origmode = os.stat(file)[stat.ST_MODE]
        newmode = origmode | stat.S_IWRITE | stat.S_IREAD
        os.chmod(file, newmode)

    # We need to extract the debug src information here...
    if debugsrcdir:
	os.system("%s'%s' -b '%s' -d '%s' -i -l '%s' '%s'" % (pathprefix, debugedit, workparentdir, debugsrcdir, sourcefile, file))

    bb.mkdirhier(os.path.dirname(debugfile))

    os.system("%s'%s' --only-keep-debug '%s' '%s'" % (pathprefix, objcopy, file, debugfile))

    # Set the debuglink to have the view of the file path on the target
    os.system("%s'%s' --add-gnu-debuglink='%s' '%s'" % (pathprefix, objcopy, debugfile, file))

    if newmode:
        os.chmod(file, origmode)

    return 0

def splitfile2(debugsrcdir, d):
    # Function to split a single file, called from split_and_strip_files below
    #
    # The debug src information processed in the splitfile2 is further procecessed
    # and copied to the destination here.

    import commands, stat

    dvar = bb.data.getVar('PKGD', d, True)
    pathprefix = "export PATH=%s; " % bb.data.getVar('PATH', d, True)
    strip = bb.data.getVar("STRIP", d, True)
    objcopy = bb.data.getVar("OBJCOPY", d, True)
    debugedit = bb.data.expand("${STAGING_LIBDIR_NATIVE}/rpm/bin/debugedit", d)
    workdir = bb.data.expand("${WORKDIR}", d)
    workparentdir = os.path.dirname(workdir)
    workbasedir = os.path.basename(workdir)
    sourcefile = bb.data.expand("${WORKDIR}/debugsources.list", d)

    if debugsrcdir:
       bb.mkdirhier("%s%s" % (dvar, debugsrcdir))

       processdebugsrc =  "LC_ALL=C ; sort -z -u '%s' | egrep -v -z '(<internal>|<built-in>)$' | "
       # We need to ignore files that are not actually ours
       # we do this by only paying attention to items from this package
       processdebugsrc += "fgrep -z '%s' | "
       processdebugsrc += "(cd '%s' ; cpio -pd0mL --no-preserve-owner '%s%s' 2>/dev/null)"

       os.system(processdebugsrc % (sourcefile, workbasedir, workparentdir, dvar, debugsrcdir))

       # The copy by cpio may have resulted in some empty directories!  Remove these
       for root, dirs, files in os.walk("%s%s" % (dvar, debugsrcdir)):
          for d in dirs:
              dir = os.path.join(root, d)
              #bb.note("rmdir -p %s" % dir)
              os.system("rmdir -p %s 2>/dev/null" % dir)

def runstrip(file, elftype, d):
    # Function to strip a single file, called from split_and_strip_files below
    # A working 'file' (one which works on the target architecture)
    #
    # The elftype is a bit pattern (explained in split_and_strip_files) to tell
    # us what type of file we're processing...
    # 4 - executable
    # 8 - shared library

    import commands, stat

    pathprefix = "export PATH=%s; " % bb.data.getVar('PATH', d, True)
    strip = bb.data.getVar("STRIP", d, True)

    # Handle kernel modules specifically - .debug directories here are pointless
    if file.find("/lib/modules/") != -1 and file.endswith(".ko"):
        return os.system("%s'%s' --strip-debug --remove-section=.comment --remove-section=.note --preserve-dates '%s'" % (pathprefix, strip, file))

    newmode = None
    if not os.access(file, os.W_OK) or os.access(file, os.R_OK):
        origmode = os.stat(file)[stat.ST_MODE]
        newmode = origmode | stat.S_IWRITE | stat.S_IREAD
        os.chmod(file, newmode)

    extraflags = ""
    # .so and shared library
    if ".so" in file and elftype & 8:
        extraflags = "--remove-section=.comment --remove-section=.note --strip-unneeded"
    # shared or executable:
    elif elftype & 8 or elftype & 4:
        extraflags = "--remove-section=.comment --remove-section=.note"

    stripcmd = "'%s' %s '%s'" % (strip, extraflags, file)
    bb.debug(1, "runstrip: %s" % stripcmd)

    ret = os.system("%s%s" % (pathprefix, stripcmd))

    if newmode:
        os.chmod(file, origmode)

    if ret:
        bb.error("runstrip: '%s' strip command failed" % stripcmd)

    return 0

#
# Package data handling routines
#

def get_package_mapping (pkg, d):
	import oe.packagedata

	data = oe.packagedata.read_subpkgdata(pkg, d)
	key = "PKG_%s" % pkg

	if key in data:
		return data[key]

	return pkg

def runtime_mapping_rename (varname, d):
	#bb.note("%s before: %s" % (varname, bb.data.getVar(varname, d, True)))	

	new_depends = []
	deps = bb.utils.explode_dep_versions(bb.data.getVar(varname, d, True) or "")
	for depend in deps:
		# Have to be careful with any version component of the depend
		new_depend = get_package_mapping(depend, d)
		if deps[depend]:
			new_depends.append("%s (%s)" % (new_depend, deps[depend]))
		else:
			new_depends.append(new_depend)

	bb.data.setVar(varname, " ".join(new_depends) or None, d)

	#bb.note("%s after: %s" % (varname, bb.data.getVar(varname, d, True)))

#
# Package functions suitable for inclusion in PACKAGEFUNCS
#

python package_get_auto_pr() {
	if d.getVar('USE_PR_SERV', True) != "0":
		auto_pr=prserv_get_pr_auto(d)
		if auto_pr is None:
			bb.fatal("Can NOT get auto PR revision from remote PR service")
			return
		d.setVar('PRAUTO',str(auto_pr))
}

python package_do_split_locales() {
	if (bb.data.getVar('PACKAGE_NO_LOCALE', d, True) == '1'):
		bb.debug(1, "package requested not splitting locales")
		return

	packages = (bb.data.getVar('PACKAGES', d, True) or "").split()

	datadir = bb.data.getVar('datadir', d, True)
	if not datadir:
		bb.note("datadir not defined")
		return

	dvar = bb.data.getVar('PKGD', d, True)
	pn = bb.data.getVar('PN', d, True)

	if pn + '-locale' in packages:
		packages.remove(pn + '-locale')

	localedir = os.path.join(dvar + datadir, 'locale')

	if not os.path.isdir(localedir):
		bb.debug(1, "No locale files in this package")
		return

	locales = os.listdir(localedir)

	# This is *really* broken
	mainpkg = packages[0]
	# At least try and patch it up I guess...
	if mainpkg.find('-dbg'):
		mainpkg = mainpkg.replace('-dbg', '')
	if mainpkg.find('-dev'):
		mainpkg = mainpkg.replace('-dev', '')

	summary = bb.data.getVar('SUMMARY', d, True) or pn
	description = bb.data.getVar('DESCRIPTION', d, True) or "" 
        locale_section = bb.data.getVar('LOCALE_SECTION', d, True)
	for l in locales:
		ln = legitimize_package_name(l)
		pkg = pn + '-locale-' + ln
		packages.append(pkg)
		bb.data.setVar('FILES_' + pkg, os.path.join(datadir, 'locale', l), d)
		bb.data.setVar('RDEPENDS_' + pkg, '%s virtual-locale-%s' % (mainpkg, ln), d)
		bb.data.setVar('RPROVIDES_' + pkg, '%s-locale %s-translation' % (pn, ln), d)
		bb.data.setVar('SUMMARY_' + pkg, '%s - %s translations' % (summary, l), d)
		bb.data.setVar('DESCRIPTION_' + pkg, '%s  This package contains language translation files for the %s locale.' % (description, l), d)
		if locale_section:
			bb.data.setVar('SECTION_' + pkg, locale_section, d)

	bb.data.setVar('PACKAGES', ' '.join(packages), d)

	# Disabled by RP 18/06/07
	# Wildcards aren't supported in debian
	# They break with ipkg since glibc-locale* will mean that 
	# glibc-localedata-translit* won't install as a dependency
	# for some other package which breaks meta-toolchain
	# Probably breaks since virtual-locale- isn't provided anywhere
	#rdep = (bb.data.getVar('RDEPENDS_%s' % mainpkg, d, True) or bb.data.getVar('RDEPENDS', d, True) or "").split()
	#rdep.append('%s-locale*' % pn)
	#bb.data.setVar('RDEPENDS_%s' % mainpkg, ' '.join(rdep), d)
}

python perform_packagecopy () {
	dest = bb.data.getVar('D', d, True)
	dvar = bb.data.getVar('PKGD', d, True)

	bb.mkdirhier(dvar)

	# Start by package population by taking a copy of the installed 
	# files to operate on
	os.system('rm -rf %s/*' % (dvar))
	# Preserve sparse files and hard links
	os.system('tar -cf - -C %s -ps . | tar -xf - -C %s' % (dest, dvar))
}

# We generate a master list of directories to process, we start by
# seeding this list with reasonable defaults, then load from
# the fs-perms.txt files
python fixup_perms () {
	import os, pwd, grp

	# init using a string with the same format as a line as documented in
	# the fs-perms.txt file
	# <path> <mode> <uid> <gid> <walk> <fmode> <fuid> <fgid>
	# <path> link <link target>
	#
	# __str__ can be used to print out an entry in the input format
	#
	# if fs_perms_entry.path is None:
	#	an error occured
	# if fs_perms_entry.link, you can retrieve:
	#	fs_perms_entry.path = path
	#	fs_perms_entry.link = target of link
	# if not fs_perms_entry.link, you can retrieve:
	#	fs_perms_entry.path = path
	#	fs_perms_entry.mode = expected dir mode or None
	#	fs_perms_entry.uid = expected uid or -1
	#	fs_perms_entry.gid = expected gid or -1
	#	fs_perms_entry.walk = 'true' or something else
	#	fs_perms_entry.fmode = expected file mode or None
	#	fs_perms_entry.fuid = expected file uid or -1
	#	fs_perms_entry_fgid = expected file gid or -1
	class fs_perms_entry():
		def __init__(self, line):
			lsplit = line.split()
			if len(lsplit) == 3 and lsplit[1].lower() == "link":
				self._setlink(lsplit[0], lsplit[2])
			elif len(lsplit) == 8:
				self._setdir(lsplit[0], lsplit[1], lsplit[2], lsplit[3], lsplit[4], lsplit[5], lsplit[6], lsplit[7])
			else:
				bb.error("Fixup Perms: invalid config line %s" % line)
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
		fs_perms_tables = bb.data.getVar('FILESYSTEM_PERMS_TABLES', d, True)
		if not fs_perms_tables:
			fs_perms_tables = 'files/fs-perms.txt'
		for conf_file in fs_perms_tables.split():
			str += " %s" % bb.which(bb.data.getVar('BBPATH', d, True), conf_file)
		return str



	dvar = bb.data.getVar('PKGD', d, True)

	fs_perms_table = {}

	# By default all of the standard directories specified in
	# bitbake.conf will get 0755 root:root.
	target_path_vars = [	'base_prefix',
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
		dir = bb.data.getVar(path, d, True) or ""
		if dir == "":
			continue
		fs_perms_table[dir] = fs_perms_entry(bb.data.expand("%s 0755 root root false - - -" % (dir), d))

	# Now we actually load from the configuration files
	for conf in get_fs_perms_list(d).split():
		if os.path.exists(conf):
			f = open(conf)
			for line in f:
				if line.startswith('#'):
					continue
				lsplit = line.split()
				if len(lsplit) == 0:
					continue
				if len(lsplit) != 8 and not (len(lsplit) == 3 and lsplit[1].lower() == "link"):
					bb.error("Fixup perms: %s invalid line: %s" % (conf, line))
					continue
				entry = fs_perms_entry(bb.data.expand(line, d))
				if entry and entry.path:
					fs_perms_table[entry.path] = entry
			f.close()

	# Debug -- list out in-memory table
	#for dir in fs_perms_table:
	#	bb.note("Fixup Perms: %s: %s" % (dir, str(fs_perms_table[dir])))

	# We process links first, so we can go back and fixup directory ownership
	# for any newly created directories
	for dir in fs_perms_table:
		if not fs_perms_table[dir].link:
			continue

		origin = dvar + dir
		if not (os.path.exists(origin) and os.path.isdir(origin) and not os.path.islink(origin)):
			continue

		link = fs_perms_table[dir].link
		if link[0] == "/":
			target = dvar + link
			ptarget = link
		else:
			target = os.path.join(os.path.dirname(origin), link)
			ptarget = os.path.join(os.path.dirname(dir), link)
		if os.path.exists(target):
			bb.error("Fixup Perms: Unable to correct directory link, target already exists: %s -> %s" % (dir, ptarget))
			continue

		# Create path to move directory to, move it, and then setup the symlink
		bb.mkdirhier(os.path.dirname(target))
		#bb.note("Fixup Perms: Rename %s -> %s" % (dir, ptarget))
		os.rename(origin, target)
		#bb.note("Fixup Perms: Link %s -> %s" % (dir, link))
		os.symlink(link, origin)

	for dir in fs_perms_table:
		if fs_perms_table[dir].link:
			continue

		origin = dvar + dir
		if not (os.path.exists(origin) and os.path.isdir(origin)):
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
}

python split_and_strip_files () {
	import commands, stat, errno

	dvar = bb.data.getVar('PKGD', d, True)
	pn = bb.data.getVar('PN', d, True)

	# We default to '.debug' style
	if bb.data.getVar('PACKAGE_DEBUG_SPLIT_STYLE', d, True) == 'debug-file-directory':
		# Single debug-file-directory style debug info
		debugappend = ".debug"
		debugdir = ""
		debuglibdir = "/usr/lib/debug"
		debugsrcdir = "/usr/src/debug"
	else:
		# Original Poky, a.k.a. ".debug", style debug info
		debugappend = ""
		debugdir = "/.debug"
		debuglibdir = ""
		debugsrcdir = "/usr/src/debug"

	os.chdir(dvar)

	# Return type (bits):
	# 0 - not elf
	# 1 - ELF
	# 2 - stripped
	# 4 - executable
	# 8 - shared library
	def isELF(path):
		type = 0
		pathprefix = "export PATH=%s; " % bb.data.getVar('PATH', d, True)
		ret, result = commands.getstatusoutput("%sfile '%s'" % (pathprefix, path))

		if ret:
			bb.error("split_and_strip_files: 'file %s' failed" % path)
			return type

		# Not stripped
		if "ELF" in result:
			type |= 1
			if "not stripped" not in result:
				type |= 2
			if "executable" in result:
				type |= 4
			if "shared" in result:
				type |= 8
		return type


	#
	# First lets figure out all of the files we may have to process ... do this only once!
	#
	file_list = {}
	file_links = {}
	if (bb.data.getVar('INHIBIT_PACKAGE_DEBUG_SPLIT', d, True) != '1') and \
	   (bb.data.getVar('INHIBIT_PACKAGE_STRIP', d, True) != '1'):
		for root, dirs, files in os.walk(dvar):
			for f in files:
				file = os.path.join(root, f)
				# Only process files (and symlinks)... Skip files that are obviously debug files
				if not (debugappend != "" and file.endswith(debugappend)) and \
				   not (debugdir != "" and debugdir in os.path.dirname(file[len(dvar):])) and \
				   os.path.isfile(file):
					try:
						s = os.stat(file)
					except OSError, (err, strerror):
						if err != errno.ENOENT:
							raise
						# Skip broken symlinks
						continue
					# Is the item excutable?  Then we need to process it.
					if (s[stat.ST_MODE] & stat.S_IXUSR) or \
					   (s[stat.ST_MODE] & stat.S_IXGRP) or \
					   (s[stat.ST_MODE] & stat.S_IXOTH):
						# If it's a symlink, and points to an ELF file, we capture the readlink target
						if os.path.islink(file):
							target = os.readlink(file)
							if not os.path.isabs(target):
								ltarget = os.path.join(os.path.dirname(file), target)
							else:
								ltarget = target

							if isELF(ltarget):
								#bb.note("Sym: %s (%d)" % (ltarget, isELF(ltarget)))
								file_list[file] = "sym: " + target
							continue
						# It's a file (or hardlink), not a link
						# ...but is it ELF, and is it already stripped?
						elf_file = isELF(file)
						if elf_file & 1:
							# Check if it's a hard link to something else
							if s.st_nlink > 1:
								file_reference = "%d_%d" % (s.st_dev, s.st_ino)
								# Hard link to something else
								file_list[file] = "hard: " + file_reference
								continue

							file_list[file] = "ELF: %d" % elf_file


	#
	# First lets process debug splitting
	#
	if (bb.data.getVar('INHIBIT_PACKAGE_DEBUG_SPLIT', d, True) != '1'):
		for file in file_list:
			src = file[len(dvar):]
			dest = debuglibdir + os.path.dirname(src) + debugdir + "/" + os.path.basename(src) + debugappend
			fpath = dvar + dest
			# Preserve symlinks in debug area...
			if file_list[file].startswith("sym: "):
				ltarget = file_list[file][5:]
				lpath = os.path.dirname(ltarget)
				lbase = os.path.basename(ltarget)
				ftarget = ""
				if lpath and lpath != ".":
					ftarget += lpath + debugdir + "/"
				ftarget += lbase + debugappend
				bb.mkdirhier(os.path.dirname(fpath))
				#bb.note("Symlink %s -> %s" % (fpath, ftarget))
				os.symlink(ftarget, fpath)
				continue

			# Preserve hard links in debug area...
			file_reference = ""
			if file_list[file].startswith("hard: "):
				file_reference = file_list[file][6:]
				if file_reference not in file_links:
					# If this is a new file, add it as a reference, and
					# update it's type, so we can fall through and split
					file_list[file] = "ELF: %d" % (isELF(file))
				else:
					target = file_links[file_reference][len(dvar):]
					ftarget = dvar + debuglibdir + os.path.dirname(target) + debugdir + "/" + os.path.basename(target) + debugappend
					bb.mkdirhier(os.path.dirname(fpath))
					#bb.note("Link %s -> %s" % (fpath, ftarget))
					os.link(ftarget, fpath)
					continue

			# It's ELF...
			if file_list[file].startswith("ELF: "):
				elf_file = int(file_list[file][5:])
				if elf_file & 2:
					bb.warn("File '%s' from %s was already stripped, this will prevent future debugging!" % (src, pn))
					continue

				# Split the file...
				bb.mkdirhier(os.path.dirname(fpath))
				#bb.note("Split %s -> %s" % (file, fpath))
				# Only store off the hard link reference if we successfully split!
				if splitfile(file, fpath, debugsrcdir, d) == 0 and file_reference != "":
					file_links[file_reference] = file

		# The above may have generated dangling symlinks, remove them!
		# Dangling symlinks are a result of something NOT being split, such as a stripped binary.
		# This should be a rare occurance, but we want to clean up anyway.
		for file in file_list:
			if file_list[file].startswith("sym: "):
				src = file[len(dvar):]
				dest = debuglibdir + os.path.dirname(src) + debugdir + "/" + os.path.basename(src) + debugappend
				fpath = dvar + dest
				try:
					s = os.stat(fpath)
				except OSError, (err, strerror):
					if err != errno.ENOENT:
						raise
					#bb.note("Remove dangling link %s -> %s" % (fpath, os.readlink(fpath)))
					os.unlink(fpath)
					# This could leave an empty debug directory laying around
					# take care of the obvious case...
					os.system("rmdir %s 2>/dev/null" % os.path.dirname(fpath))

		# Process the debugsrcdir if requested...
		# This copies and places the referenced sources for later debugging...
		splitfile2(debugsrcdir, d)
	#
	# End of debug splitting
	#

	#
	# Now lets go back over things and strip them
	#
	if (bb.data.getVar('INHIBIT_PACKAGE_STRIP', d, True) != '1'):	
		for file in file_list:
			if file_list[file].startswith("ELF: "):
				elf_file = int(file_list[file][5:])
				#bb.note("Strip %s" % file)
				runstrip(file, elf_file, d)
	#
	# End of strip
	#
}

python populate_packages () {
	import glob, stat, errno, re

	workdir = bb.data.getVar('WORKDIR', d, True)
	outdir = bb.data.getVar('DEPLOY_DIR', d, True)
	dvar = bb.data.getVar('PKGD', d, True)
	packages = bb.data.getVar('PACKAGES', d, True)
	pn = bb.data.getVar('PN', d, True)

	bb.mkdirhier(outdir)
	os.chdir(dvar)

	# Sanity check PACKAGES for duplicates - should be moved to 
	# sanity.bbclass once we have the infrastucture
	package_list = []
	for pkg in packages.split():
		if pkg in package_list:
			bb.error("-------------------")
			bb.error("%s is listed in PACKAGES multiple times, this leads to packaging errors." % pkg)
			bb.error("Please fix the metadata/report this as bug to OE bugtracker.")
			bb.error("-------------------")
		else:
			package_list.append(pkg)

	pkgdest = bb.data.getVar('PKGDEST', d, True)
	os.system('rm -rf %s' % pkgdest)

	seen = []

	for pkg in package_list:
		localdata = bb.data.createCopy(d)
		root = os.path.join(pkgdest, pkg)
		bb.mkdirhier(root)

		bb.data.setVar('PKG', pkg, localdata)
		overrides = bb.data.getVar('OVERRIDES', localdata, True)
		if not overrides:
			raise bb.build.FuncFailed('OVERRIDES not defined')
		bb.data.setVar('OVERRIDES', overrides + ':' + pkg, localdata)
		bb.data.update_data(localdata)

		filesvar = bb.data.getVar('FILES', localdata, True) or ""
		files = filesvar.split()
		file_links = {}
		for file in files:
			if os.path.isabs(file):
				file = '.' + file
			if not os.path.islink(file):
				if os.path.isdir(file):
					newfiles =  [ os.path.join(file,x) for x in os.listdir(file) ]
					if newfiles:
						files += newfiles
						continue
			globbed = glob.glob(file)
			if globbed:
				if [ file ] != globbed:
					files += globbed
					continue
			if (not os.path.islink(file)) and (not os.path.exists(file)):
				continue
			if file in seen:
				continue
			seen.append(file)

			def mkdir(src, dest, p):
				src = os.path.join(src, p)
				dest = os.path.join(dest, p)
				bb.mkdirhier(dest)
				fstat = os.stat(src)
				os.chmod(dest, fstat.st_mode)
				os.chown(dest, fstat.st_uid, fstat.st_gid)
				if p not in seen:
					seen.append(p)

			def mkdir_recurse(src, dest, paths):
				while paths.startswith("./"):
					paths = paths[2:]
				p = "."
				for c in paths.split("/"):
					p = os.path.join(p, c)
					if not os.path.exists(os.path.join(dest, p)):
						mkdir(src, dest, p)

			if os.path.isdir(file) and not os.path.islink(file):
				mkdir_recurse(dvar, root, file)
				continue

			mkdir_recurse(dvar, root, os.path.dirname(file))
			fpath = os.path.join(root,file)
			if not os.path.islink(file):
				os.link(file, fpath)
				fstat = os.stat(file)
				os.chmod(fpath, fstat.st_mode)
				os.chown(fpath, fstat.st_uid, fstat.st_gid)
				continue
			ret = bb.copyfile(file, fpath)
			if ret is False or ret == 0:
				raise bb.build.FuncFailed("File population failed")

		del localdata
	os.chdir(workdir)

	unshipped = []
	for root, dirs, files in os.walk(dvar):
		dir = root[len(dvar):]
		if not dir:
			dir = os.sep
		for f in (files + dirs):
			path = os.path.join(dir, f)
			if ('.' + path) not in seen:
				unshipped.append(path)

	if unshipped != []:
		bb.warn("For recipe %s, the following files/directories were installed but not shipped in any package:" % pn)
		for f in unshipped:
			bb.warn("  " + f)

	bb.build.exec_func("package_name_hook", d)

	for pkg in package_list:
		pkgname = bb.data.getVar('PKG_%s' % pkg, d, True)
		if pkgname is None:
			bb.data.setVar('PKG_%s' % pkg, pkg, d)

	dangling_links = {}
	pkg_files = {}
	for pkg in package_list:
		dangling_links[pkg] = []
		pkg_files[pkg] = []
		inst_root = os.path.join(pkgdest, pkg)
		for root, dirs, files in os.walk(inst_root):
			for f in files:
				path = os.path.join(root, f)
				rpath = path[len(inst_root):]
				pkg_files[pkg].append(rpath)
				try:
					s = os.stat(path)
				except OSError, (err, strerror):
					if err != errno.ENOENT:
						raise
					target = os.readlink(path)
					if target[0] != '/':
						target = os.path.join(root[len(inst_root):], target)
					dangling_links[pkg].append(os.path.normpath(target))

	for pkg in package_list:
		rdepends = bb.utils.explode_dep_versions(bb.data.getVar('RDEPENDS_' + pkg, d, True) or bb.data.getVar('RDEPENDS', d, True) or "")

		for l in dangling_links[pkg]:
			found = False
			bb.debug(1, "%s contains dangling link %s" % (pkg, l))
			for p in package_list:
				for f in pkg_files[p]:
					if f == l:
						found = True
						bb.debug(1, "target found in %s" % p)
						if p == pkg:
							break
						if p not in rdepends:
							rdepends[p] = ""
						break
			if found == False:
				bb.note("%s contains dangling symlink to %s" % (pkg, l))
		bb.data.setVar('RDEPENDS_' + pkg, bb.utils.join_deps(rdepends, commasep=False), d)
}
populate_packages[dirs] = "${D}"

PKGDESTWORK = "${WORKDIR}/pkgdata"

python emit_pkgdata() {
	from glob import glob

	def write_if_exists(f, pkg, var):
		def encode(str):
			import codecs
			c = codecs.getencoder("string_escape")
			return c(str)[0]

		val = bb.data.getVar('%s_%s' % (var, pkg), d, True)
		if val:
			f.write('%s_%s: %s\n' % (var, pkg, encode(val)))
			return
		val = bb.data.getVar('%s' % (var), d, True)
		if val:
			f.write('%s: %s\n' % (var, encode(val)))
		return

	packages = bb.data.getVar('PACKAGES', d, True)
	pkgdest = bb.data.getVar('PKGDEST', d, 1)
	pkgdatadir = bb.data.getVar('PKGDESTWORK', d, True)

	# Take shared lock since we're only reading, not writing
	lf = bb.utils.lockfile(bb.data.expand("${PACKAGELOCK}", d), True)

	data_file = pkgdatadir + bb.data.expand("/${PN}" , d)
	f = open(data_file, 'w')
	f.write("PACKAGES: %s\n" % packages)
	f.close()

	workdir = bb.data.getVar('WORKDIR', d, True)

	for pkg in packages.split():
		subdata_file = pkgdatadir + "/runtime/%s" % pkg

		sf = open(subdata_file, 'w')
		write_if_exists(sf, pkg, 'PN')
		write_if_exists(sf, pkg, 'PV')
		write_if_exists(sf, pkg, 'PR')
		write_if_exists(sf, pkg, 'PKGV')
		write_if_exists(sf, pkg, 'PKGR')
		write_if_exists(sf, pkg, 'DESCRIPTION')
		write_if_exists(sf, pkg, 'SUMMARY')
		write_if_exists(sf, pkg, 'RDEPENDS')
		write_if_exists(sf, pkg, 'RPROVIDES')
		write_if_exists(sf, pkg, 'RRECOMMENDS')
		write_if_exists(sf, pkg, 'RSUGGESTS')
		write_if_exists(sf, pkg, 'RREPLACES')
		write_if_exists(sf, pkg, 'RCONFLICTS')
		write_if_exists(sf, pkg, 'SECTION')
		write_if_exists(sf, pkg, 'PKG')
		write_if_exists(sf, pkg, 'ALLOW_EMPTY')
		write_if_exists(sf, pkg, 'FILES')
		write_if_exists(sf, pkg, 'pkg_postinst')
		write_if_exists(sf, pkg, 'pkg_postrm')
		write_if_exists(sf, pkg, 'pkg_preinst')
		write_if_exists(sf, pkg, 'pkg_prerm')
		write_if_exists(sf, pkg, 'FILERPROVIDESFLIST')
		for dfile in (bb.data.getVar('FILERPROVIDESFLIST_' + pkg, d, True) or "").split():
			write_if_exists(sf, pkg, 'FILERPROVIDES_' + dfile)

		write_if_exists(sf, pkg, 'FILERDEPENDSFLIST')
		for dfile in (bb.data.getVar('FILERDEPENDSFLIST_' + pkg, d, True) or "").split():
			write_if_exists(sf, pkg, 'FILERDEPENDS_' + dfile)

		sf.close()


		allow_empty = bb.data.getVar('ALLOW_EMPTY_%s' % pkg, d, True)
		if not allow_empty:
			allow_empty = bb.data.getVar('ALLOW_EMPTY', d, True)
		root = "%s/%s" % (pkgdest, pkg)
		os.chdir(root)
		g = glob('*')
		if g or allow_empty == "1":
			packagedfile = pkgdatadir + '/runtime/%s.packaged' % pkg
			file(packagedfile, 'w').close()

	bb.utils.unlockfile(lf)
}
emit_pkgdata[dirs] = "${PKGDESTWORK}/runtime"

ldconfig_postinst_fragment() {
if [ x"$D" = "x" ]; then
	[ -x /sbin/ldconfig ] && /sbin/ldconfig
fi
}

RPMDEPS = "${STAGING_LIBDIR_NATIVE}/rpm/bin/rpmdeps  --define '_rpmfc_magic_path ${STAGING_DIR_NATIVE}/usr/share/misc/magic.mgc'"

# Collect perfile run-time dependency metadata
# Output:
#  FILERPROVIDESFLIST_pkg - list of all files w/ deps
#  FILERPROVIDES_filepath_pkg - per file dep
#
#  FILERDEPENDSFLIST_pkg - list of all files w/ deps
#  FILERDEPENDS_filepath_pkg - per file dep

python package_do_filedeps() {
	import os, re

	pkgdest = bb.data.getVar('PKGDEST', d, True)
	packages = bb.data.getVar('PACKAGES', d, True)

	rpmdeps = bb.data.expand("${RPMDEPS}", d)
	r = re.compile(r'[<>=]+ +[^ ]*')

	# Quick routine to process the results of the rpmdeps call...
	def process_deps(pipe, pkg, f, provides_files, requires_files):
		provides = []
		requires = []
		file = f.replace(pkgdest + "/" + pkg, "")
		file = file.replace("@", "@at@")
		file = file.replace(" ", "@space@")
		file = file.replace("\t", "@tab@")
		file = file.replace("[", "@openbrace@")
		file = file.replace("]", "@closebrace@")
		file = file.replace("_", "@underscore@")

		for line in pipe:
			if line.startswith("Requires:"):
				i = requires
			elif line.startswith("Provides:"):
				i = provides
			else:
				continue
			value = line.split(":", 1)[1].strip()
			value = r.sub(r'(\g<0>)', value)
			if value.startswith("rpmlib("):
				continue
			i.append(value)

		if len(provides) > 0:
			provides_files.append(file)
			key = "FILERPROVIDES_" + file + "_" + pkg
			bb.data.setVar(key, " ".join(provides), d)

		if len(requires) > 0:
			requires_files.append(file)
			key = "FILERDEPENDS_" + file + "_" + pkg
			bb.data.setVar(key, " ".join(requires), d)

	# Determine dependencies
	for pkg in packages.split():
		if pkg.endswith('-dbg') or pkg.endswith('-doc') or pkg.find('-locale-') != -1 or pkg.find('-localedata-') != -1 or pkg.find('-gconv-') != -1 or pkg.find('-charmap-') != -1 or pkg.startswith('kernel-module-'):
			continue

		provides_files = []
		requires_files = []
		for root, dirs, files in os.walk(pkgdest + "/" + pkg):
			for file in files:
				f = os.path.join(root, file)

				dep_pipe = os.popen(rpmdeps + " --provides --requires -v " + f)

				process_deps(dep_pipe, pkg, f, provides_files, requires_files)

		bb.data.setVar("FILERDEPENDSFLIST_" + pkg, " ".join(requires_files), d)
		bb.data.setVar("FILERPROVIDESFLIST_" + pkg, " ".join(provides_files), d)
}

SHLIBSDIR = "${STAGING_DIR_HOST}/shlibs"
SHLIBSWORKDIR = "${WORKDIR}/shlibs"

python package_do_shlibs() {
	import re

	exclude_shlibs = bb.data.getVar('EXCLUDE_FROM_SHLIBS', d, 0)
	if exclude_shlibs:
		bb.note("not generating shlibs")
		return
		
	lib_re = re.compile("^.*\.so")
	libdir_re = re.compile(".*/lib$")

	packages = bb.data.getVar('PACKAGES', d, True)
	targetos = bb.data.getVar('TARGET_OS', d, True)

	workdir = bb.data.getVar('WORKDIR', d, True)

	ver = bb.data.getVar('PKGV', d, True)
	if not ver:
		bb.error("PKGV not defined")
		return

	pkgdest = bb.data.getVar('PKGDEST', d, True)

	shlibs_dir = bb.data.getVar('SHLIBSDIR', d, True)
	shlibswork_dir = bb.data.getVar('SHLIBSWORKDIR', d, True)

	# Take shared lock since we're only reading, not writing
	lf = bb.utils.lockfile(bb.data.expand("${PACKAGELOCK}", d))

	def linux_so(root, path, file):
		cmd = bb.data.getVar('OBJDUMP', d, True) + " -p " + os.path.join(root, file) + " 2>/dev/null"
		cmd = "PATH=\"%s\" %s" % (bb.data.getVar('PATH', d, True), cmd)
		fd = os.popen(cmd)
		lines = fd.readlines()
		fd.close()
		for l in lines:
			m = re.match("\s+NEEDED\s+([^\s]*)", l)
			if m:
				needed[pkg].append(m.group(1))
			m = re.match("\s+SONAME\s+([^\s]*)", l)
			if m:
				this_soname = m.group(1)
				if not this_soname in sonames:
					# if library is private (only used by package) then do not build shlib for it
					if not private_libs or -1 == private_libs.find(this_soname):
						sonames.append(this_soname)
				if libdir_re.match(root):
					needs_ldconfig = True
				if snap_symlinks and (file != this_soname):
					renames.append((os.path.join(root, file), os.path.join(root, this_soname)))

	def darwin_so(root, path, file):
		fullpath = os.path.join(root, file)
		if not os.path.exists(fullpath):
			return

		def get_combinations(base):
			#
			# Given a base library name, find all combinations of this split by "." and "-"
			#
			combos = []
			options = base.split(".")
			for i in range(1, len(options) + 1):
				combos.append(".".join(options[0:i]))
			options = base.split("-")
			for i in range(1, len(options) + 1):
				combos.append("-".join(options[0:i]))
			return combos		

		if (file.endswith('.dylib') or file.endswith('.so')) and not pkg.endswith('-dev') and not pkg.endswith('-dbg'):
			# Drop suffix
			name = file.rsplit(".",1)[0]
			# Find all combinations
			combos = get_combinations(name)
			for combo in combos:
				if not combo in sonames:
					sonames.append(combo)
		if file.endswith('.dylib') or file.endswith('.so'):
			lafile = fullpath.replace(os.path.join(pkgdest, pkg), bb.data.getVar('PKGD', d, True))
			# Drop suffix
			lafile = lafile.rsplit(".",1)[0]
			lapath = os.path.dirname(lafile)
			lafile = os.path.basename(lafile)
			# Find all combinations
			combos = get_combinations(lafile)
			for combo in combos:
				if os.path.exists(lapath + '/' + combo + '.la'):
					break
			lafile = lapath + '/' + combo + '.la'

			#bb.note("Foo2: %s" % lafile)
			#bb.note("Foo %s %s" % (file, fullpath))
			if os.path.exists(lafile):
				fd = open(lafile, 'r')
				lines = fd.readlines()
				fd.close()
				for l in lines:
					m = re.match("\s*dependency_libs=\s*'(.*)'", l)
					if m:
						deps = m.group(1).split(" ")
						for dep in deps:
							#bb.note("Trying %s for %s" % (dep, pkg))
							name = None
							if dep.endswith(".la"):
								name = os.path.basename(dep).replace(".la", "")
							elif dep.startswith("-l"):
								name = dep.replace("-l", "lib")
							if pkg not in needed:
								needed[pkg] = []
							if name:
								needed[pkg].append(name)
								#bb.note("Adding %s for %s" % (name, pkg))

	if bb.data.getVar('PACKAGE_SNAP_LIB_SYMLINKS', d, True) == "1":
		snap_symlinks = True
	else:
		snap_symlinks = False

	if (bb.data.getVar('USE_LDCONFIG', d, True) or "1") == "1":
		use_ldconfig = True
	else:
		use_ldconfig = False

	needed = {}
	shlib_provider = {}
	private_libs = bb.data.getVar('PRIVATE_LIBS', d, True)
	for pkg in packages.split():
		needs_ldconfig = False
		bb.debug(2, "calculating shlib provides for %s" % pkg)

		pkgver = bb.data.getVar('PKGV_' + pkg, d, True)
		if not pkgver:
			pkgver = bb.data.getVar('PV_' + pkg, d, True)
		if not pkgver:
			pkgver = ver

		needed[pkg] = []
		sonames = list()
		renames = list()
		top = os.path.join(pkgdest, pkg)
		for root, dirs, files in os.walk(top):
			for file in files:
				soname = None
				path = os.path.join(root, file)
				if os.path.islink(path):
					continue
				if targetos == "darwin" or targetos == "darwin8":
					darwin_so(root, dirs, file)
				elif os.access(path, os.X_OK) or lib_re.match(file):
					linux_so(root, dirs, file)
		for (old, new) in renames:
		    	bb.note("Renaming %s to %s" % (old, new))
			os.rename(old, new)
		shlibs_file = os.path.join(shlibswork_dir, pkg + ".list")
		shver_file = os.path.join(shlibswork_dir, pkg + ".ver")
		if len(sonames):
			fd = open(shlibs_file, 'w')
			for s in sonames:
				fd.write(s + '\n')
				shlib_provider[s] = (pkg, pkgver)
			fd.close()
			fd = open(shver_file, 'w')
			fd.write(pkgver + '\n')
			fd.close()
		if needs_ldconfig and use_ldconfig:
			bb.debug(1, 'adding ldconfig call to postinst for %s' % pkg)
			postinst = bb.data.getVar('pkg_postinst_%s' % pkg, d, True) or bb.data.getVar('pkg_postinst', d, True)
			if not postinst:
				postinst = '#!/bin/sh\n'
			postinst += bb.data.getVar('ldconfig_postinst_fragment', d, True)
			bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)

	list_re = re.compile('^(.*)\.list$')
	for dir in [shlibs_dir]: 
		if not os.path.exists(dir):
			continue
		for file in os.listdir(dir):
			m = list_re.match(file)
			if m:
				dep_pkg = m.group(1)
				fd = open(os.path.join(dir, file))
				lines = fd.readlines()
				fd.close()
				ver_file = os.path.join(dir, dep_pkg + '.ver')
				lib_ver = None
				if os.path.exists(ver_file):
					fd = open(ver_file)
					lib_ver = fd.readline().rstrip()
					fd.close()
				for l in lines:
					shlib_provider[l.rstrip()] = (dep_pkg, lib_ver)

	bb.utils.unlockfile(lf)

	assumed_libs = bb.data.getVar('ASSUME_SHLIBS', d, True)
	if assumed_libs:
	    for e in assumed_libs.split():
		l, dep_pkg = e.split(":")
		lib_ver = None
		dep_pkg = dep_pkg.rsplit("_", 1)
		if len(dep_pkg) == 2:
		    lib_ver = dep_pkg[1]
		dep_pkg = dep_pkg[0]
		shlib_provider[l] = (dep_pkg, lib_ver)

	for pkg in packages.split():
		bb.debug(2, "calculating shlib requirements for %s" % pkg)

		deps = list()
		for n in needed[pkg]:
			if n in shlib_provider.keys():
				(dep_pkg, ver_needed) = shlib_provider[n]

				if dep_pkg == pkg:
					continue

				if ver_needed:
					dep = "%s (>= %s)" % (dep_pkg, ver_needed)
				else:
					dep = dep_pkg
				if not dep in deps:
					deps.append(dep)
			else:
				bb.note("Couldn't find shared library provider for %s" % n)

		deps_file = os.path.join(pkgdest, pkg + ".shlibdeps")
		if os.path.exists(deps_file):
			os.remove(deps_file)
		if len(deps):
			fd = open(deps_file, 'w')
			for dep in deps:
				fd.write(dep + '\n')
			fd.close()
}

python package_do_pkgconfig () {
	import re

	packages = bb.data.getVar('PACKAGES', d, True)
	workdir = bb.data.getVar('WORKDIR', d, True)
	pkgdest = bb.data.getVar('PKGDEST', d, True)

	shlibs_dir = bb.data.getVar('SHLIBSDIR', d, True)
	shlibswork_dir = bb.data.getVar('SHLIBSWORKDIR', d, True)

	pc_re = re.compile('(.*)\.pc$')
	var_re = re.compile('(.*)=(.*)')
	field_re = re.compile('(.*): (.*)')

	pkgconfig_provided = {}
	pkgconfig_needed = {}
	for pkg in packages.split():
		pkgconfig_provided[pkg] = []
		pkgconfig_needed[pkg] = []
		top = os.path.join(pkgdest, pkg)
		for root, dirs, files in os.walk(top):
			for file in files:
				m = pc_re.match(file)
				if m:
					pd = bb.data.init()
					name = m.group(1)
					pkgconfig_provided[pkg].append(name)
					path = os.path.join(root, file)
					if not os.access(path, os.R_OK):
						continue
					f = open(path, 'r')
					lines = f.readlines()
					f.close()
					for l in lines:
						m = var_re.match(l)
						if m:
							name = m.group(1)
							val = m.group(2)
							bb.data.setVar(name, bb.data.expand(val, pd), pd)
							continue
						m = field_re.match(l)
						if m:
							hdr = m.group(1)
							exp = bb.data.expand(m.group(2), pd)
							if hdr == 'Requires':
								pkgconfig_needed[pkg] += exp.replace(',', ' ').split()

	# Take shared lock since we're only reading, not writing
	lf = bb.utils.lockfile(bb.data.expand("${PACKAGELOCK}", d))

	for pkg in packages.split():
		pkgs_file = os.path.join(shlibswork_dir, pkg + ".pclist")
		if pkgconfig_provided[pkg] != []:
			f = open(pkgs_file, 'w')
			for p in pkgconfig_provided[pkg]:
				f.write('%s\n' % p)
			f.close()

	for dir in [shlibs_dir]:
		if not os.path.exists(dir):
			continue
		for file in os.listdir(dir):
			m = re.match('^(.*)\.pclist$', file)
			if m:
				pkg = m.group(1)
				fd = open(os.path.join(dir, file))
				lines = fd.readlines()
				fd.close()
				pkgconfig_provided[pkg] = []
				for l in lines:
					pkgconfig_provided[pkg].append(l.rstrip())

	for pkg in packages.split():
		deps = []
		for n in pkgconfig_needed[pkg]:
			found = False
			for k in pkgconfig_provided.keys():
				if n in pkgconfig_provided[k]:
					if k != pkg and not (k in deps):
						deps.append(k)
					found = True
			if found == False:
				bb.note("couldn't find pkgconfig module '%s' in any package" % n)
		deps_file = os.path.join(pkgdest, pkg + ".pcdeps")
		if len(deps):
			fd = open(deps_file, 'w')
			for dep in deps:
				fd.write(dep + '\n')
			fd.close()

	bb.utils.unlockfile(lf)
}

python read_shlibdeps () {
	packages = bb.data.getVar('PACKAGES', d, True).split()
	for pkg in packages:
		rdepends = bb.utils.explode_dep_versions(bb.data.getVar('RDEPENDS_' + pkg, d, 0) or bb.data.getVar('RDEPENDS', d, 0) or "")

		for extension in ".shlibdeps", ".pcdeps", ".clilibdeps":
			depsfile = bb.data.expand("${PKGDEST}/" + pkg + extension, d)
			if os.access(depsfile, os.R_OK):
				fd = file(depsfile)
				lines = fd.readlines()
				fd.close()
				for l in lines:
					rdepends[l.rstrip()] = ""
		bb.data.setVar('RDEPENDS_' + pkg, bb.utils.join_deps(rdepends, commasep=False), d)
}

python package_depchains() {
	"""
	For a given set of prefix and postfix modifiers, make those packages
	RRECOMMENDS on the corresponding packages for its RDEPENDS.

	Example:  If package A depends upon package B, and A's .bb emits an
	A-dev package, this would make A-dev Recommends: B-dev.

	If only one of a given suffix is specified, it will take the RRECOMMENDS
	based on the RDEPENDS of *all* other packages. If more than one of a given 
	suffix is specified, its will only use the RDEPENDS of the single parent 
	package.
	"""

	packages  = bb.data.getVar('PACKAGES', d, True)
	postfixes = (bb.data.getVar('DEPCHAIN_POST', d, True) or '').split()
	prefixes  = (bb.data.getVar('DEPCHAIN_PRE', d, True) or '').split()

	def pkg_adddeprrecs(pkg, base, suffix, getname, depends, d):

		#bb.note('depends for %s is %s' % (base, depends))
		rreclist = bb.utils.explode_dep_versions(bb.data.getVar('RRECOMMENDS_' + pkg, d, True) or bb.data.getVar('RRECOMMENDS', d, True) or "")

		for depend in depends:
			if depend.find('-native') != -1 or depend.find('-cross') != -1 or depend.startswith('virtual/'):
				#bb.note("Skipping %s" % depend)
				continue
			if depend.endswith('-dev'):
				depend = depend.replace('-dev', '')
			if depend.endswith('-dbg'):
				depend = depend.replace('-dbg', '')
			pkgname = getname(depend, suffix)
			#bb.note("Adding %s for %s" % (pkgname, depend))
			if pkgname not in rreclist:
				rreclist[pkgname] = ""

		#bb.note('setting: RRECOMMENDS_%s=%s' % (pkg, ' '.join(rreclist)))
		bb.data.setVar('RRECOMMENDS_%s' % pkg, bb.utils.join_deps(rreclist, commasep=False), d)

	def pkg_addrrecs(pkg, base, suffix, getname, rdepends, d):

		#bb.note('rdepends for %s is %s' % (base, rdepends))
		rreclist = bb.utils.explode_dep_versions(bb.data.getVar('RRECOMMENDS_' + pkg, d, True) or bb.data.getVar('RRECOMMENDS', d, True) or "")

		for depend in rdepends:
			if depend.find('virtual-locale-') != -1:
				#bb.note("Skipping %s" % depend)
				continue
			if depend.endswith('-dev'):
				depend = depend.replace('-dev', '')
			if depend.endswith('-dbg'):
				depend = depend.replace('-dbg', '')
			pkgname = getname(depend, suffix)
			#bb.note("Adding %s for %s" % (pkgname, depend))
			if pkgname not in rreclist:
				rreclist[pkgname] = ""

		#bb.note('setting: RRECOMMENDS_%s=%s' % (pkg, ' '.join(rreclist)))
		bb.data.setVar('RRECOMMENDS_%s' % pkg, bb.utils.join_deps(rreclist, commasep=False), d)

	def add_dep(list, dep):
		dep = dep.split(' (')[0].strip()
		if dep not in list:
			list.append(dep)

	depends = []
	for dep in bb.utils.explode_deps(bb.data.getVar('DEPENDS', d, True) or ""):
		add_dep(depends, dep)

	rdepends = []
	for dep in bb.utils.explode_deps(bb.data.getVar('RDEPENDS', d, True) or ""):
		add_dep(rdepends, dep)

	for pkg in packages.split():
		for dep in bb.utils.explode_deps(bb.data.getVar('RDEPENDS_' + pkg, d, True) or ""):
			add_dep(rdepends, dep)

	#bb.note('rdepends is %s' % rdepends)

	def post_getname(name, suffix):
		return '%s%s' % (name, suffix)
	def pre_getname(name, suffix):
		return '%s%s' % (suffix, name)

	pkgs = {}
	for pkg in packages.split():
		for postfix in postfixes:
			if pkg.endswith(postfix):
				if not postfix in pkgs:
					pkgs[postfix] = {}
				pkgs[postfix][pkg] = (pkg[:-len(postfix)], post_getname)

		for prefix in prefixes:
			if pkg.startswith(prefix):
				if not prefix in pkgs:
					pkgs[prefix] = {}
				pkgs[prefix][pkg] = (pkg[:-len(prefix)], pre_getname)

	for suffix in pkgs:
		for pkg in pkgs[suffix]:
			if bb.data.getVarFlag('RRECOMMENDS_' + pkg, 'nodeprrecs', d):
				continue
			(base, func) = pkgs[suffix][pkg]
			if suffix == "-dev":
				pkg_adddeprrecs(pkg, base, suffix, func, depends, d)
			if len(pkgs[suffix]) == 1:
				pkg_addrrecs(pkg, base, suffix, func, rdepends, d)
			else:
				rdeps = []
				for dep in bb.utils.explode_deps(bb.data.getVar('RDEPENDS_' + base, d, True) or bb.data.getVar('RDEPENDS', d, True) or ""):
					add_dep(rdeps, dep)
				pkg_addrrecs(pkg, base, suffix, func, rdeps, d)
}

# Since bitbake can't determine which variables are accessed during package 
# iteration, we need to list them here:
PACKAGEVARS = "FILES RDEPENDS RRECOMMENDS SUMMARY DESCRIPTION RSUGGESTS RPROVIDES RCONFLICTS PKG ALLOW_EMPTY pkg_postinst pkg_postrm INITSCRIPT_NAME INITSCRIPT_PARAMS DEBIAN_NOAUTONAME"

def gen_packagevar(d):
    ret = []
    pkgs = (d.getVar("PACKAGES", True) or "").split()
    vars = (d.getVar("PACKAGEVARS", True) or "").split()
    for p in pkgs:
        for v in vars:
            ret.append(v + "_" + p)
    return " ".join(ret)

PACKAGE_PREPROCESS_FUNCS ?= ""
PACKAGEFUNCS ?= "package_get_auto_pr \	
                perform_packagecopy \
                ${PACKAGE_PREPROCESS_FUNCS} \
		package_do_split_locales \
		split_and_strip_files \
		fixup_perms \
		populate_packages \
		package_do_filedeps \
		package_do_shlibs \
		package_do_pkgconfig \
		read_shlibdeps \
		package_depchains \
		emit_pkgdata"

python do_package () {
        # Change the following version to cause sstate to invalidate the package
        # cache.  This is useful if an item this class depends on changes in a
        # way that the output of this class changes.  rpmdeps is a good example
        # as any change to rpmdeps requires this to be rerun.
        # PACKAGE_BBCLASS_VERSION = "1"

	packages = (bb.data.getVar('PACKAGES', d, True) or "").split()
	if len(packages) < 1:
		bb.debug(1, "No packages to build, skipping do_package")
		return

	workdir = bb.data.getVar('WORKDIR', d, True)
	outdir = bb.data.getVar('DEPLOY_DIR', d, True)
	dest = bb.data.getVar('D', d, True)
	dvar = bb.data.getVar('PKGD', d, True)
	pn = bb.data.getVar('PN', d, True)

	if not workdir or not outdir or not dest or not dvar or not pn or not packages:
		bb.error("WORKDIR, DEPLOY_DIR, D, PN and PKGD all must be defined, unable to package")
		return

	for f in (bb.data.getVar('PACKAGEFUNCS', d, True) or '').split():
		bb.build.exec_func(f, d)
}

do_package[dirs] = "${SHLIBSWORKDIR} ${PKGDESTWORK} ${D}"
do_package[vardeps] += "${PACKAGEFUNCS} ${@gen_packagevar(d)}"
addtask package before do_build after do_install

PACKAGELOCK = "${STAGING_DIR}/package-output.lock"
SSTATETASKS += "do_package"
do_package[sstate-name] = "package"
do_package[sstate-plaindirs] = "${PKGD} ${PKGDEST}"
do_package[sstate-inputdirs] = "${PKGDESTWORK} ${SHLIBSWORKDIR}"
do_package[sstate-outputdirs] = "${PKGDATA_DIR} ${SHLIBSDIR}"
do_package[sstate-lockfile-shared] = "${PACKAGELOCK}"
do_package[stamp-extra-info] = "${MACHINE}"
do_package_setscene[dirs] = "${STAGING_DIR}"

python do_package_setscene () {
	sstate_setscene(d)
}
addtask do_package_setscene

# Dummy task to mark when all packaging is complete
do_package_write () {
	:
}
do_package_write[noexec] = "1"
PACKAGERDEPTASK = "do_package_write"
do_build[recrdeptask] += "${PACKAGERDEPTASK}"
addtask package_write before do_build after do_package

#
# Helper functions for the package writing classes
#

python package_mapping_rename_hook () {
	"""
	Rewrite variables to account for package renaming in things
	like debian.bbclass or manual PKG variable name changes
	"""
	runtime_mapping_rename("RDEPENDS", d)
	runtime_mapping_rename("RRECOMMENDS", d)
	runtime_mapping_rename("RSUGGESTS", d)
	runtime_mapping_rename("RPROVIDES", d)
	runtime_mapping_rename("RREPLACES", d)
	runtime_mapping_rename("RCONFLICTS", d)
}

EXPORT_FUNCTIONS mapping_rename_hook
