# Must inherit package first before changing PACKAGEFUNCS
inherit package
PACKAGEFUNCS += "emit_pkghistory"

PKGHIST_DIR = "${TMPDIR}/pkghistory/${BASEPKG_TARGET_SYS}/"


#
# Called during do_package to write out metadata about this package
# for comparision when writing future packages
#
python emit_pkghistory() {
	packages = bb.data.getVar('PACKAGES', d, True)
	pkghistdir = bb.data.getVar('PKGHIST_DIR', d, True)


	# Should check PACKAGES here to see if anything removed

	def getpkgvar(pkg, var):
		val = bb.data.getVar('%s_%s' % (var, pkg), d, 1)
		if val:
			return val
		val = bb.data.getVar('%s' % (var), d, 1)

		return val

	def getlastversion(pkg):
		try:
			pe = os.path.basename(os.readlink(os.path.join(pkghistdir, pkg, "latest")))
			pv = os.path.basename(os.readlink(os.path.join(pkghistdir, pkg, pe, "latest")))
			pr = os.path.basename(os.readlink(os.path.join(pkghistdir, pkg, pe, pv, "latest")))
			return (pe, pv, pr)
		except OSError:
			return (None, None, None)			

	for pkg in packages.split():
		pe = getpkgvar(pkg, 'PE') or "0"
		pv = getpkgvar(pkg, 'PV')
		pr = getpkgvar(pkg, 'PR')
		destdir = os.path.join(pkghistdir, pkg, pe, pv, pr)
		
		#
		# Find out what the last version was
		# Make sure the version did not decrease
		#
		lastversion = getlastversion(pkg)
		(last_pe, last_pv, last_pr) = lastversion

		if last_pe is not None:
			r = bb.utils.vercmp((pe, pv, pr), lastversion)
			if r < 0:
				bb.fatal("Package version for package %s went backwards which would break package feeds from (%s:%s-%s to %s:%s-%s)" % (pkg, last_pe, last_pv, last_pr, pe, pv, pr))

		write_pkghistory(pkg, pe, pv, pr, d)

		if last_pe is not None:
			check_pkghistory(pkg, pe, pv, pr, lastversion)

		write_latestlink(pkg, pe, pv, pr, d)		
}


def check_pkghistory(pkg, pe, pv, pr, lastversion):
	(last_pe, last_pv, last_pr) = lastversion

	bb.debug(2, "Checking package history")
	# RDEPENDS removed?
	# PKG changed?
	# Each file list of each package for file removals?


def write_pkghistory(pkg, pe, pv, pr, d):
	bb.debug(2, "Writing package history")

	pkghistdir = bb.data.getVar('PKGHIST_DIR', d, True)

	verpath = os.path.join(pkghistdir, pkg, pe, pv, pr)
	if not os.path.exists(verpath):
		os.makedirs(verpath)

def write_latestlink(pkg, pe, pv, pr, d):
	pkghistdir = bb.data.getVar('PKGHIST_DIR', d, True)

	def rm_link(path):
		try: 
			os.unlink(path)
		except OSError:
			return

	rm_link(os.path.join(pkghistdir, pkg, "latest"))
	rm_link(os.path.join(pkghistdir, pkg, pe, "latest"))
	rm_link(os.path.join(pkghistdir, pkg, pe, pv, "latest"))

	os.symlink(os.path.join(pkghistdir, pkg, pe), os.path.join(pkghistdir, pkg, "latest"))
	os.symlink(os.path.join(pkghistdir, pkg, pe, pv), os.path.join(pkghistdir, pkg, pe, "latest"))
	os.symlink(os.path.join(pkghistdir, pkg, pe, pv, pr), os.path.join(pkghistdir, pkg, pe, pv, "latest"))

