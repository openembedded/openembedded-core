#
# General packaging help functions
#

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

def do_split_packages(d, root, file_regex, output_pattern, description, postinst=None, recursive=False, hook=None, extra_depends=None, aux_files_pattern=None, postrm=None, allow_dirs=False, prepend=False, match_path=False, aux_files_pattern_verbatim=None):
	"""
	Used in .bb files to split up dynamically generated subpackages of a 
	given package, usually plugins or modules.
	"""
	import os, os.path, bb

	dvar = bb.data.getVar('D', d, 1)
	if not dvar:
		bb.error("D not defined")
		return

	packages = bb.data.getVar('PACKAGES', d, 1).split()
	if not packages:
		# nothing to do
		return

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
		if not (stat.S_ISREG(mode) or (allow_dirs and stat.S_ISDIR(mode))):
			continue
		on = legitimize_package_name(m.group(1))
		pkg = output_pattern % on
		if not pkg in packages:
			if prepend:
				packages = [pkg] + packages
			else:
				packages.append(pkg)
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
				the_depends = bb.data.getVar('RDEPENDS_' + pkg, d, 1)
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
			oldfiles = bb.data.getVar('FILES_' + pkg, d, 1)
			if not oldfiles:
				bb.fatal("Package '%s' exists but has no files" % pkg)
			bb.data.setVar('FILES_' + pkg, oldfiles + " " + os.path.join(root, o), d)
		if callable(hook):
			hook(f, pkg, file_regex, output_pattern, m.group(1))

	bb.data.setVar('PACKAGES', ' '.join(packages), d)

python () {
    import bb

    if bb.data.getVar('PACKAGES', d, 1) != '':
        bb.data.setVarFlag('do_package', 'depends', 'file-native:do_populate_staging', d)
        deps = bb.data.getVarFlag('do_package_write', 'depends', d) or ""
        for dep in (bb.data.getVar('PACKAGE_EXTRA_DEPENDS', d, 1) or "").split():
            deps += " %s:do_populate_staging" % dep
        bb.data.setVarFlag('do_package_write', 'depends', deps, d)
}

# file(1) output to match to consider a file an unstripped executable
FILE_UNSTRIPPED_MATCH ?= "not stripped"
#FIXME: this should be "" when any errors are gone!
IGNORE_STRIP_ERRORS ?= "1"

runstrip() {
	# Function to strip a single file, called from RUNSTRIP in populate_packages below
	# A working 'file' (one which works on the target architecture)
	# is necessary for this stuff to work, hence the addition to do_package[depends]

	local ro st

	st=0
	if {	file "$1" || {
			oewarn "file $1: failed (forced strip)" >&2
			echo '${FILE_UNSTRIPPED_MATCH}'
		}
	   } | grep -q '${FILE_UNSTRIPPED_MATCH}'
	then
		oenote "${STRIP} $1"
		ro=
		test -w "$1" || {
			ro=1
			chmod +w "$1"
		}
		mkdir -p $(dirname "$1")/.debug
		debugfile="$(dirname "$1")/.debug/$(basename "$1")"
		'${OBJCOPY}' --only-keep-debug "$1" "$debugfile"
		'${STRIP}' "$1"
		st=$?
		'${OBJCOPY}' --add-gnu-debuglink="$debugfile" "$1"
		test -n "$ro" && chmod -w "$1"
		if test $st -ne 0
		then
			oewarn "runstrip: ${STRIP} $1: strip failed" >&2
			if [ x${IGNORE_STRIP_ERRORS} = x1 ]
			then
				#FIXME: remove this, it's for error detection
				if file "$1" 2>/dev/null >&2
				then
					(oefatal "${STRIP} $1: command failed" >/dev/tty)
				else
					(oefatal "file $1: command failed" >/dev/tty)
				fi
				st=0
			fi
		fi
	else
		oenote "runstrip: skip $1"
	fi
	return $st
}


#
# Package data handling routines
#

STAGING_PKGMAPS_DIR ?= "${STAGING_DIR}/pkgmaps"

def add_package_mapping (pkg, new_name, d):
	import bb, os

	def encode(str):
		import codecs
		c = codecs.getencoder("string_escape")
		return c(str)[0]

	pmap_dir = bb.data.getVar('STAGING_PKGMAPS_DIR', d, 1)

	bb.mkdirhier(pmap_dir)

	data_file = os.path.join(pmap_dir, pkg)

	f = open(data_file, 'w')
	f.write("%s\n" % encode(new_name))
	f.close()

def get_package_mapping (pkg, d):
	import bb, os

	def decode(str):
		import codecs
		c = codecs.getdecoder("string_escape")
		return c(str)[0]

	data_file = bb.data.expand("${STAGING_PKGMAPS_DIR}/%s" % pkg, d)

	if os.access(data_file, os.R_OK):
		f = file(data_file, 'r')
		lines = f.readlines()
		f.close()
		for l in lines:
			return decode(l).strip()
	return pkg

def runtime_mapping_rename (varname, d):
	import bb, os

	#bb.note("%s before: %s" % (varname, bb.data.getVar(varname, d, 1)))	

	new_depends = []
	for depend in explode_deps(bb.data.getVar(varname, d, 1) or ""):
		# Have to be careful with any version component of the depend
		split_depend = depend.split(' (')
		new_depend = get_package_mapping(split_depend[0].strip(), d)
		if len(split_depend) > 1:
			new_depends.append("%s (%s" % (new_depend, split_depend[1]))
		else:
			new_depends.append(new_depend)

	bb.data.setVar(varname, " ".join(new_depends) or None, d)

	#bb.note("%s after: %s" % (varname, bb.data.getVar(varname, d, 1)))

#
# Package functions suitable for inclusion in PACKAGEFUNCS
#

python package_do_split_locales() {
	import os

	if (bb.data.getVar('PACKAGE_NO_LOCALE', d, 1) == '1'):
		bb.debug(1, "package requested not splitting locales")
		return

	packages = (bb.data.getVar('PACKAGES', d, 1) or "").split()
	if not packages:
		bb.debug(1, "no packages to build; not splitting locales")
		return

	datadir = bb.data.getVar('datadir', d, 1)
	if not datadir:
		bb.note("datadir not defined")
		return

	dvar = bb.data.getVar('D', d, 1)
	if not dvar:
		bb.error("D not defined")
		return

	pn = bb.data.getVar('PN', d, 1)
	if not pn:
		bb.error("PN not defined")
		return

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

	for l in locales:
		ln = legitimize_package_name(l)
		pkg = pn + '-locale-' + ln
		packages.append(pkg)
		bb.data.setVar('FILES_' + pkg, os.path.join(datadir, 'locale', l), d)
		bb.data.setVar('RDEPENDS_' + pkg, '%s virtual-locale-%s' % (mainpkg, ln), d)
		bb.data.setVar('RPROVIDES_' + pkg, '%s-locale %s-translation' % (pn, ln), d)
		bb.data.setVar('DESCRIPTION_' + pkg, '%s translation for %s' % (l, pn), d)

	bb.data.setVar('PACKAGES', ' '.join(packages), d)

	rdep = (bb.data.getVar('RDEPENDS_%s' % mainpkg, d, 1) or bb.data.getVar('RDEPENDS', d, 1) or "").split()
	rdep.append('%s-locale*' % pn)
	bb.data.setVar('RDEPENDS_%s' % mainpkg, ' '.join(rdep), d)
}

python populate_packages () {
	import glob, stat, errno, re

	workdir = bb.data.getVar('WORKDIR', d, 1)
	if not workdir:
		bb.error("WORKDIR not defined, unable to package")
		return

	import os # path manipulations
	outdir = bb.data.getVar('DEPLOY_DIR', d, 1)
	if not outdir:
		bb.error("DEPLOY_DIR not defined, unable to package")
		return
	bb.mkdirhier(outdir)

	dvar = bb.data.getVar('D', d, 1)
	if not dvar:
		bb.error("D not defined, unable to package")
		return
	bb.mkdirhier(dvar)

	packages = bb.data.getVar('PACKAGES', d, 1)
	if not packages:
		bb.debug(1, "PACKAGES not defined, nothing to package")
		return

	pn = bb.data.getVar('PN', d, 1)
	if not pn:
		bb.error("PN not defined")
		return

	os.chdir(dvar)

	def isexec(path):
		try:
			s = os.stat(path)
		except (os.error, AttributeError):
			return 0
		return (s[stat.ST_MODE] & stat.S_IEXEC)

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

	if (bb.data.getVar('INHIBIT_PACKAGE_STRIP', d, 1) != '1'):
		stripfunc = ""
		for root, dirs, files in os.walk(dvar):
			for f in files:
				file = os.path.join(root, f)
				if not os.path.islink(file) and not os.path.isdir(file) and isexec(file):
					stripfunc += "\trunstrip %s || st=1\n" % (file)
		if not stripfunc == "":
			from bb import build
			localdata = bb.data.createCopy(d)
			# strip
			bb.data.setVar('RUNSTRIP', '\tlocal st\n\tst=0\n%s\treturn $st' % stripfunc, localdata)
			bb.data.setVarFlag('RUNSTRIP', 'func', 1, localdata)
			bb.build.exec_func('RUNSTRIP', localdata)

	for pkg in package_list:
		localdata = bb.data.createCopy(d)
		root = os.path.join(workdir, "install", pkg)

		os.system('rm -rf %s' % root)

		bb.data.setVar('ROOT', '', localdata)
		bb.data.setVar('ROOT_%s' % pkg, root, localdata)
		bb.data.setVar('PKG', pkg, localdata)

		overrides = bb.data.getVar('OVERRIDES', localdata, 1)
		if not overrides:
			raise bb.build.FuncFailed('OVERRIDES not defined')
		bb.data.setVar('OVERRIDES', overrides+':'+pkg, localdata)

		bb.data.update_data(localdata)

		root = bb.data.getVar('ROOT', localdata, 1)
		bb.mkdirhier(root)
		filesvar = bb.data.getVar('FILES', localdata, 1) or ""
		files = filesvar.split()
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
			fpath = os.path.join(root,file)
			dpath = os.path.dirname(fpath)
			bb.mkdirhier(dpath)
			ret = bb.movefile(file,fpath)
			if ret is None or ret == 0:
				raise bb.build.FuncFailed("File population failed")
		del localdata
	os.chdir(workdir)

	unshipped = []
	for root, dirs, files in os.walk(dvar):
		for f in files:
			path = os.path.join(root[len(dvar):], f)
			unshipped.append(path)

	if unshipped != []:
		bb.note("the following files were installed but not shipped in any package:")
		for f in unshipped:
			bb.note("  " + f)

	bb.build.exec_func("package_name_hook", d)

	for pkg in package_list:
		pkgname = bb.data.getVar('PKG_%s' % pkg, d, 1)
		if pkgname is None:
			bb.data.setVar('PKG_%s' % pkg, pkg, d)
		else:
			add_package_mapping(pkg, pkgname, d)

	dangling_links = {}
	pkg_files = {}
	for pkg in package_list:
		dangling_links[pkg] = []
		pkg_files[pkg] = []
		inst_root = os.path.join(workdir, "install", pkg)
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
		rdepends = explode_deps(bb.data.getVar('RDEPENDS_' + pkg, d, 1) or bb.data.getVar('RDEPENDS', d, 1) or "")
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
						if not p in rdepends:
							rdepends.append(p)
						break
			if found == False:
				bb.note("%s contains dangling symlink to %s" % (pkg, l))
		bb.data.setVar('RDEPENDS_' + pkg, " " + " ".join(rdepends), d)
}
populate_packages[dirs] = "${D}"

python emit_pkgdata() {
	def write_if_exists(f, pkg, var):
		def encode(str):
			import codecs
			c = codecs.getencoder("string_escape")
			return c(str)[0]

		val = bb.data.getVar('%s_%s' % (var, pkg), d, 1)
		if val:
			f.write('%s_%s: %s\n' % (var, pkg, encode(val)))

	packages = bb.data.getVar('PACKAGES', d, 1)
	if not packages:
		return

	data_file = bb.data.expand("${STAGING_DIR}/pkgdata/${PN}", d)
	f = open(data_file, 'w')
	f.write("PACKAGES: %s\n" % packages)
	f.close()

	for pkg in packages.split():
		subdata_file = bb.data.expand("${STAGING_DIR}/pkgdata/runtime/%s" % pkg, d)
		sf = open(subdata_file, 'w')
		write_if_exists(sf, pkg, 'DESCRIPTION')
		write_if_exists(sf, pkg, 'RDEPENDS')
		write_if_exists(sf, pkg, 'RPROVIDES')
		write_if_exists(sf, pkg, 'RRECOMMENDS')
		write_if_exists(sf, pkg, 'RSUGGESTS')
		write_if_exists(sf, pkg, 'RPROVIDES')
		write_if_exists(sf, pkg, 'RREPLACES')
		write_if_exists(sf, pkg, 'RCONFLICTS')
		write_if_exists(sf, pkg, 'PKG')
		write_if_exists(sf, pkg, 'ALLOW_EMPTY')
		write_if_exists(sf, pkg, 'FILES')
		write_if_exists(sf, pkg, 'pkg_postinst')
		write_if_exists(sf, pkg, 'pkg_postrm')
		write_if_exists(sf, pkg, 'pkg_preinst')
		write_if_exists(sf, pkg, 'pkg_prerm')
		sf.close()
}
emit_pkgdata[dirs] = "${STAGING_DIR}/pkgdata/runtime"

ldconfig_postinst_fragment() {
if [ x"$D" = "x" ]; then
	ldconfig
fi
}

python package_do_shlibs() {
	import os, re, os.path

	exclude_shlibs = bb.data.getVar('EXCLUDE_FROM_SHLIBS', d, 0)
	if exclude_shlibs:
		bb.note("not generating shlibs")
		return
		
	lib_re = re.compile("^lib.*\.so")
	libdir_re = re.compile(".*/lib$")

	packages = bb.data.getVar('PACKAGES', d, 1)
	if not packages:
		bb.debug(1, "no packages to build; not calculating shlibs")
		return

	workdir = bb.data.getVar('WORKDIR', d, 1)
	if not workdir:
		bb.error("WORKDIR not defined")
		return

	staging = bb.data.getVar('STAGING_DIR', d, 1)
	if not staging:
		bb.error("STAGING_DIR not defined")
		return

	ver = bb.data.getVar('PV', d, 1)
	if not ver:
		bb.error("PV not defined")
		return

	target_sys = bb.data.getVar('TARGET_SYS', d, 1)
	if not target_sys:
		bb.error("TARGET_SYS not defined")
		return

	shlibs_dir = os.path.join(staging, target_sys, "shlibs")
	old_shlibs_dir = os.path.join(staging, "shlibs")
	bb.mkdirhier(shlibs_dir)

	needed = {}
	for pkg in packages.split():
		needs_ldconfig = False
		bb.debug(2, "calculating shlib provides for %s" % pkg)

		needed[pkg] = []
		sonames = list()
		top = os.path.join(workdir, "install", pkg)
		for root, dirs, files in os.walk(top):
			for file in files:
				soname = None
				path = os.path.join(root, file)
				if os.access(path, os.X_OK) or lib_re.match(file):
					cmd = (bb.data.getVar('BUILD_PREFIX', d, 1) or "") + "objdump -p " + path + " 2>/dev/null"
					fd = os.popen(cmd)
					lines = fd.readlines()
					fd.close()
					for l in lines:
						m = re.match("\s+NEEDED\s+([^\s]*)", l)
						if m:
							needed[pkg].append(m.group(1))
						m = re.match("\s+SONAME\s+([^\s]*)", l)
						if m and not m.group(1) in sonames:
							sonames.append(m.group(1))
						if m and libdir_re.match(root):
							needs_ldconfig = True
		shlibs_file = os.path.join(shlibs_dir, pkg + ".list")
		if os.path.exists(shlibs_file):
			os.remove(shlibs_file)
		shver_file = os.path.join(shlibs_dir, pkg + ".ver")
		if os.path.exists(shver_file):
			os.remove(shver_file)
		if len(sonames):
			fd = open(shlibs_file, 'w')
			for s in sonames:
				fd.write(s + '\n')
			fd.close()
			fd = open(shver_file, 'w')
			fd.write(ver + '\n')
			fd.close()
		if needs_ldconfig:
			bb.debug(1, 'adding ldconfig call to postinst for %s' % pkg)
			postinst = bb.data.getVar('pkg_postinst_%s' % pkg, d, 1) or bb.data.getVar('pkg_postinst', d, 1)
			if not postinst:
				postinst = '#!/bin/sh\n'
			postinst += bb.data.getVar('ldconfig_postinst_fragment', d, 1)
			bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)

	shlib_provider = {}
	list_re = re.compile('^(.*)\.list$')
	for dir in [old_shlibs_dir, shlibs_dir]: 
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

		deps_file = os.path.join(workdir, "install", pkg + ".shlibdeps")
		if os.path.exists(deps_file):
			os.remove(deps_file)
		if len(deps):
			fd = open(deps_file, 'w')
			for dep in deps:
				fd.write(dep + '\n')
			fd.close()
}

python package_do_pkgconfig () {
	import re, os

	packages = bb.data.getVar('PACKAGES', d, 1)
	if not packages:
		bb.debug(1, "no packages to build; not calculating pkgconfig dependencies")
		return

	workdir = bb.data.getVar('WORKDIR', d, 1)
	if not workdir:
		bb.error("WORKDIR not defined")
		return

	staging = bb.data.getVar('STAGING_DIR', d, 1)
	if not staging:
		bb.error("STAGING_DIR not defined")
		return

	target_sys = bb.data.getVar('TARGET_SYS', d, 1)
	if not target_sys:
		bb.error("TARGET_SYS not defined")
		return

	shlibs_dir = os.path.join(staging, target_sys, "shlibs")
	old_shlibs_dir = os.path.join(staging, "shlibs")
	bb.mkdirhier(shlibs_dir)

	pc_re = re.compile('(.*)\.pc$')
	var_re = re.compile('(.*)=(.*)')
	field_re = re.compile('(.*): (.*)')

	pkgconfig_provided = {}
	pkgconfig_needed = {}
	for pkg in packages.split():
		pkgconfig_provided[pkg] = []
		pkgconfig_needed[pkg] = []
		top = os.path.join(workdir, "install", pkg)
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

	for pkg in packages.split():
		pkgs_file = os.path.join(shlibs_dir, pkg + ".pclist")
		if os.path.exists(pkgs_file):
			os.remove(pkgs_file)
		if pkgconfig_provided[pkg] != []:
			f = open(pkgs_file, 'w')
			for p in pkgconfig_provided[pkg]:
				f.write('%s\n' % p)
			f.close()

	for dir in [old_shlibs_dir, shlibs_dir]:
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
		deps_file = os.path.join(workdir, "install", pkg + ".pcdeps")
		if os.path.exists(deps_file):
			os.remove(deps_file)
		if len(deps):
			fd = open(deps_file, 'w')
			for dep in deps:
				fd.write(dep + '\n')
			fd.close()
}

python read_shlibdeps () {
	packages = (bb.data.getVar('PACKAGES', d, 1) or "").split()
	for pkg in packages:
		rdepends = explode_deps(bb.data.getVar('RDEPENDS_' + pkg, d, 0) or bb.data.getVar('RDEPENDS', d, 0) or "")
		shlibsfile = bb.data.expand("${WORKDIR}/install/" + pkg + ".shlibdeps", d)
		if os.access(shlibsfile, os.R_OK):
			fd = file(shlibsfile)
			lines = fd.readlines()
			fd.close()
			for l in lines:
				rdepends.append(l.rstrip())
		pcfile = bb.data.expand("${WORKDIR}/install/" + pkg + ".pcdeps", d)
		if os.access(pcfile, os.R_OK):
			fd = file(pcfile)
			lines = fd.readlines()
			fd.close()
			for l in lines:
				rdepends.append(l.rstrip())
		bb.data.setVar('RDEPENDS_' + pkg, " " + " ".join(rdepends), d)
}

python package_depchains() {
	"""
	For a given set of prefix and postfix modifiers, make those packages
	RRECOMMENDS on the corresponding packages for its DEPENDS.

	Example:  If package A depends upon package B, and A's .bb emits an
	A-dev package, this would make A-dev Recommends: B-dev.
	"""

	packages  = bb.data.getVar('PACKAGES', d, 1)
	postfixes = (bb.data.getVar('DEPCHAIN_POST', d, 1) or '').split()
	prefixes  = (bb.data.getVar('DEPCHAIN_PRE', d, 1) or '').split()

	def pkg_addrrecs(pkg, base, getname, rdepends, d):
		def packaged(pkg, d):
			return os.access(bb.data.expand('${STAGING_DIR}/pkgdata/runtime/%s.packaged' % pkg, d), os.R_OK)

		rreclist = explode_deps(bb.data.getVar('RRECOMMENDS_' + pkg, d, 1) or bb.data.getVar('RRECOMMENDS', d, 1) or "")

		for depend in rdepends:
			pkgname = getname(depend)
			if not pkgname in rreclist and packaged(pkgname, d):
				rreclist.append(pkgname)

		#bb.note('setting: RRECOMMENDS_%s=%s' % (pkg, ' '.join(rreclist)))
		bb.data.setVar('RRECOMMENDS_%s' % pkg, ' '.join(rreclist), d)

	def add_dep(list, dep):
		dep = dep.split(' (')[0].strip()
		if dep not in list:
			list.append(dep)

	rdepends = []
	for dep in explode_deps(bb.data.getVar('RDEPENDS', d, 1) or ""):
		add_dep(rdepends, dep)

	for pkg in packages.split():
		for dep in explode_deps(bb.data.getVar('RDEPENDS_' + pkg, d, 1) or ""):
			add_dep(rdepends, dep)

	#bb.note('rdepends is %s' % rdepends)

	for pkg in packages.split():
		for postfix in postfixes:
			def getname(name):
				return '%s%s' % (name, postfix)

			base = pkg[:-len(postfix)]
			if pkg.endswith(postfix):
				pkg_addrrecs(pkg, base, getname, rdepends, d)

		for prefix in prefixes:
			def getname(name):
				return '%s%s' % (prefix, name)

			base = pkg[len(prefix):]
			if pkg.startswith(prefix):
				pkg_addrrecs(pkg, base, getname, rdepends, d)
}


PACKAGEFUNCS ?= "package_do_split_locales \
		populate_packages \
		package_do_shlibs \
		package_do_pkgconfig \
		read_shlibdeps \
		package_depchains \
		emit_pkgdata"

python package_do_package () {
	for f in (bb.data.getVar('PACKAGEFUNCS', d, 1) or '').split():
		bb.build.exec_func(f, d)
}
# shlibs requires any DEPENDS to have already packaged for the *.list files
do_package[deptask] = "do_package"
do_package[dirs] = "${D}"
addtask package before do_build after do_install



PACKAGE_WRITE_FUNCS ?= "read_subpackage_metadata"

python package_do_package_write () {
	for f in (bb.data.getVar('PACKAGE_WRITE_FUNCS', d, 1) or '').split():
		bb.build.exec_func(f, d)
}
do_package_write[dirs] = "${D}"
addtask package_write before do_build after do_package


EXPORT_FUNCTIONS do_package do_package_write


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
