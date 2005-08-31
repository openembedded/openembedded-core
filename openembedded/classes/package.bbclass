def legitimize_package_name(s):
	return s.lower().replace('_', '-').replace('@', '+').replace(',', '+').replace('/', '-')

def do_split_packages(d, root, file_regex, output_pattern, description, postinst=None, recursive=False, hook=None, extra_depends=None, aux_files_pattern=None, postrm=None, allow_dirs=False, prepend=False, match_path=False):
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
		extra_depends = bb.data.getVar('PKG_' + packages[0], d, 1) or packages[0]

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

	for pkg in packages.split():
		localdata = bb.data.createCopy(d)
		root = os.path.join(workdir, "install", pkg)

		os.system('rm -rf %s' % root)

		bb.data.setVar('ROOT', '', localdata)
		bb.data.setVar('ROOT_%s' % pkg, root, localdata)
		pkgname = bb.data.getVar('PKG_%s' % pkg, localdata, 1)
		if not pkgname:
			pkgname = pkg
		bb.data.setVar('PKG', pkgname, localdata)

		overrides = bb.data.getVar('OVERRIDES', localdata, 1)
		if not overrides:
			raise bb.build.FuncFailed('OVERRIDES not defined')
		bb.data.setVar('OVERRIDES', overrides+':'+pkg, localdata)

		bb.data.update_data(localdata)

		root = bb.data.getVar('ROOT', localdata, 1)
		bb.mkdirhier(root)
		filesvar = bb.data.getVar('FILES', localdata, 1) or ""
		files = filesvar.split()
		stripfunc = ""
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
			if (bb.data.getVar('INHIBIT_PACKAGE_STRIP', d, 1) != '1') and not os.path.islink(file) and isexec(file):
				stripfunc += "${STRIP} %s || : ;\n" % fpath
			ret = bb.movefile(file,fpath)
			if ret is None or ret == 0:
				raise bb.build.FuncFailed("File population failed")
		if not stripfunc == "":
			from bb import build
			# strip
			bb.data.setVar('RUNSTRIP', '%s\nreturn 0' % stripfunc, localdata)
			bb.data.setVarFlag('RUNSTRIP', 'func', 1, localdata)
			bb.build.exec_func('RUNSTRIP', localdata)
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

	for pkg in packages.split():
		if bb.data.getVar('PKG_%s' % pkg, d, 1) is None:
			bb.data.setVar('PKG_%s' % pkg, pkg, d)

	dangling_links = {}
	pkg_files = {}
	for pkg in packages.split():
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

	for pkg in packages.split():
		rdepends = explode_deps(bb.data.getVar('RDEPENDS_' + pkg, d, 1) or bb.data.getVar('RDEPENDS', d, 1) or "")
		for l in dangling_links[pkg]:
			found = False
			bb.debug(1, "%s contains dangling link %s" % (pkg, l))
			for p in packages.split():
				for f in pkg_files[p]:
					if f == l:
						found = True
						bb.debug(1, "target found in %s" % p)
						if p == pkg:
							break
						dp = bb.data.getVar('PKG_' + p, d, 1) or p
						if not dp in rdepends:
							rdepends.append(dp)
						break
			if found == False:
				bb.note("%s contains dangling symlink to %s" % (pkg, l))
		bb.data.setVar('RDEPENDS_' + pkg, " " + " ".join(rdepends), d)

	def write_if_exists(f, pkg, var):
		def encode(str):
			import codecs
			c = codecs.getencoder("string_escape")
			return c(str)[0]

		val = bb.data.getVar('%s_%s' % (var, pkg), d, 1)
		if val:
			f.write('%s_%s: %s\n' % (var, pkg, encode(val)))

	data_file = os.path.join(workdir, "install", pn + ".package")
	f = open(data_file, 'w')
	f.write("PACKAGES: %s\n" % packages)
	for pkg in packages.split():
		write_if_exists(f, pkg, 'DESCRIPTION')
		write_if_exists(f, pkg, 'RDEPENDS')
		write_if_exists(f, pkg, 'RPROVIDES')
		write_if_exists(f, pkg, 'PKG')
		write_if_exists(f, pkg, 'ALLOW_EMPTY')
		write_if_exists(f, pkg, 'FILES')
		write_if_exists(f, pkg, 'pkg_postinst')
		write_if_exists(f, pkg, 'pkg_postrm')
	f.close()
	bb.build.exec_func("read_subpackage_metadata", d)
}

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

		pkgname = bb.data.getVar('PKG_%s' % pkg, d, 1)
		if not pkgname:
			pkgname = pkg

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
		shlibs_file = os.path.join(shlibs_dir, pkgname + ".list")
		if os.path.exists(shlibs_file):
			os.remove(shlibs_file)
		shver_file = os.path.join(shlibs_dir, pkgname + ".ver")
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

		p_pkg = bb.data.getVar("PKG_%s" % pkg, d, 1) or pkg

		deps = list()
		for n in needed[pkg]:
			if n in shlib_provider.keys():
				(dep_pkg, ver_needed) = shlib_provider[n]

				if dep_pkg == p_pkg:
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
		ppkg = bb.data.getVar("PKG_" + pkg, d, 1) or pkg
		pkgs_file = os.path.join(shlibs_dir, ppkg + ".pclist")
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

	mainpkg = packages[0]

	for l in locales:
		ln = legitimize_package_name(l)
		pkg = pn + '-locale-' + ln
		packages.append(pkg)
		bb.data.setVar('FILES_' + pkg, os.path.join(datadir, 'locale', l), d)
		bb.data.setVar('RDEPENDS_' + pkg, '${PKG_%s} virtual-locale-%s' % (mainpkg, ln), d)
		bb.data.setVar('RPROVIDES_' + pkg, '%s-locale %s-translation' % (pn, ln), d)
		bb.data.setVar('DESCRIPTION_' + pkg, '%s translation for %s' % (l, pn), d)

	bb.data.setVar('PACKAGES', ' '.join(packages), d)

	rdep = (bb.data.getVar('RDEPENDS_%s' % mainpkg, d, 1) or bb.data.getVar('RDEPENDS', d, 1) or "").split()
	rdep.append('%s-locale*' % pn)
	bb.data.setVar('RDEPENDS_%s' % mainpkg, ' '.join(rdep), d)
}

PACKAGEFUNCS = "do_install package_do_split_locales \
		populate_packages package_do_shlibs \
		package_do_pkgconfig read_shlibdeps"
python package_do_package () {
	for f in (bb.data.getVar('PACKAGEFUNCS', d, 1) or '').split():
		bb.build.exec_func(f, d)
}

do_package[dirs] = "${D}"
populate_packages[dirs] = "${D}"
EXPORT_FUNCTIONS do_package do_shlibs do_split_locales
addtask package before do_build after do_populate_staging
