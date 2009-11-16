# Debian package renaming only occurs when a package is built
# We therefore have to make sure we build all runtime packages
# before building the current package to make the packages runtime
# depends are correct
#
# Custom library package names can be defined setting
# DEBIANNAME_ + pkgname to the desired name.
#
# Better expressed as ensure all RDEPENDS package before we package
# This means we can't have circular RDEPENDS/RRECOMMENDS
do_package_write_ipk[rdeptask] = "do_package"
do_package_write_deb[rdeptask] = "do_package"
do_package_write_tar[rdeptask] = "do_package"
do_package_write_rpm[rdeptask] = "do_package"

python debian_package_name_hook () {
	import glob, copy, stat, errno, re

	pkgdest = bb.data.getVar('PKGDEST', d, 1)
	packages = bb.data.getVar('PACKAGES', d, 1)

	def socrunch(s):
		s = s.lower().replace('_', '-')
		m = re.match("^(.*)(.)\.so\.(.*)$", s)
		if m is None:
			return None
		if m.group(2) in '0123456789':
			bin = '%s%s-%s' % (m.group(1), m.group(2), m.group(3))
		else:
			bin = m.group(1) + m.group(2) + m.group(3)
		dev = m.group(1) + m.group(2)
		return (bin, dev)

	def isexec(path):
		try:
			s = os.stat(path)
		except (os.error, AttributeError):
			return 0
		return (s[stat.ST_MODE] & stat.S_IEXEC)

	def auto_libname(packages, orig_pkg):
		bin_re = re.compile(".*/s?bin$")
		lib_re = re.compile(".*/lib$")
		so_re = re.compile("lib.*\.so")
		sonames = []
		has_bins = 0
		has_libs = 0
		pkg_dir = os.path.join(pkgdest, orig_pkg)
		for root, dirs, files in os.walk(pkg_dir):
			if bin_re.match(root) and files:
				has_bins = 1
			if lib_re.match(root) and files:
				has_libs = 1
				for f in files:
					if so_re.match(f):
						fp = os.path.join(root, f)
						cmd = (bb.data.getVar('BUILD_PREFIX', d, 1) or "") + "objdump -p " + fp + " 2>/dev/null"
						fd = os.popen(cmd)
						lines = fd.readlines()
						fd.close()
						for l in lines:
							m = re.match("\s+SONAME\s+([^\s]*)", l)
							if m and not m.group(1) in sonames:
								sonames.append(m.group(1))

		bb.debug(1, 'LIBNAMES: pkg %s libs %d bins %d sonames %s' % (orig_pkg, has_libs, has_bins, sonames))
		soname = None
		if len(sonames) == 1:
			soname = sonames[0]
		elif len(sonames) > 1:
			lead = bb.data.getVar('LEAD_SONAME', d, 1)
			if lead:
				r = re.compile(lead)
				filtered = []
				for s in sonames:
					if r.match(s):
						filtered.append(s)
				if len(filtered) == 1:
					soname = filtered[0]
				elif len(filtered) > 1:
					bb.note("Multiple matches (%s) for LEAD_SONAME '%s'" % (", ".join(filtered), lead))
				else:
					bb.note("Multiple libraries (%s) found, but LEAD_SONAME '%s' doesn't match any of them" % (", ".join(sonames), lead))
			else:
				bb.note("Multiple libraries (%s) found and LEAD_SONAME not defined" % ", ".join(sonames))

		if has_libs and not has_bins and soname:
			soname_result = socrunch(soname)
			if soname_result:
				(pkgname, devname) = soname_result
				for pkg in packages.split():
					if (bb.data.getVar('PKG_' + pkg, d) or bb.data.getVar('DEBIAN_NOAUTONAME_' + pkg, d)):
						continue
					debian_pn = bb.data.getVar('DEBIANNAME_' + pkg, d)
					if debian_pn:
						newpkg = debian_pn
					elif pkg == orig_pkg:
						newpkg = pkgname
					else:
						newpkg = pkg.replace(orig_pkg, devname, 1)
					if newpkg != pkg:
						bb.data.setVar('PKG_' + pkg, newpkg, d)

	for pkg in (bb.data.getVar('AUTO_LIBNAME_PKGS', d, 1) or "").split():
		auto_libname(packages, pkg)
}

EXPORT_FUNCTIONS package_name_hook

DEBIAN_NAMES = "1"

