#
# Records history of build output in order to detect regressions
#
# Based in part on testlab.bbclass and packagehistory.bbclass
#
# Copyright (C) 2011 Intel Corporation
# Copyright (C) 2007-2011 Koen Kooi <koen@openembedded.org>
#

BUILDHISTORY_DIR ?= "${TMPDIR}/buildhistory"
BUILDHISTORY_DIR_IMAGE = "${BUILDHISTORY_DIR}/images/${MACHINE_ARCH}/${TCLIBC}/${IMAGE_BASENAME}"
BUILDHISTORY_DIR_PACKAGE = "${BUILDHISTORY_DIR}/packages/${MULTIMACH_TARGET_SYS}/${PN}"
BUILDHISTORY_COMMIT ?= "0"
BUILDHISTORY_COMMIT_AUTHOR ?= "buildhistory <buildhistory@${DISTRO}>"
BUILDHISTORY_PUSH_REPO ?= ""

# Must inherit package first before changing PACKAGEFUNCS
inherit package
PACKAGEFUNCS += "buildhistory_emit_pkghistory"

#
# Called during do_package to write out metadata about this package
# for comparision when writing future packages
#
python buildhistory_emit_pkghistory() {
	import re

	pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)

	class RecipeInfo:
		def __init__(self, name):
			self.name = name
			self.pe = "0"
			self.pv = "0"
			self.pr = "r0"
			self.depends = ""
			self.packages = ""

	class PackageInfo:
		def __init__(self, name):
			self.name = name
			self.pe = "0"
			self.pv = "0"
			self.pr = "r0"
			self.size = 0
			self.depends = ""
			self.rdepends = ""
			self.rrecommends = ""
			self.files = ""
			self.filelist = ""

	# Should check PACKAGES here to see if anything removed

	def getpkgvar(pkg, var):
		val = bb.data.getVar('%s_%s' % (var, pkg), d, 1)
		if val:
			return val
		val = bb.data.getVar('%s' % (var), d, 1)

		return val

	def readRecipeInfo(pn, histfile):
		rcpinfo = RecipeInfo(pn)
		f = open(histfile, "r")
		try:
			for line in f:
				lns = line.split('=')
				name = lns[0].strip()
				value = lns[1].strip(" \t\r\n").strip('"')
				if name == "PE":
					rcpinfo.pe = value
				elif name == "PV":
					rcpinfo.pv = value
				elif name == "PR":
					rcpinfo.pr = value
				elif name == "DEPENDS":
					rcpinfo.depends = value
				elif name == "PACKAGES":
					rcpinfo.packages = value
		finally:
			f.close()
		return rcpinfo

	def readPackageInfo(pkg, histfile):
		pkginfo = PackageInfo(pkg)
		f = open(histfile, "r")
		try:
			for line in f:
				lns = line.split('=')
				name = lns[0].strip()
				value = lns[1].strip(" \t\r\n").strip('"')
				if name == "PE":
					pkginfo.pe = value
				elif name == "PV":
					pkginfo.pv = value
				elif name == "PR":
					pkginfo.pr = value
				elif name == "RDEPENDS":
					pkginfo.rdepends = value
				elif name == "RRECOMMENDS":
					pkginfo.rrecommends = value
				elif name == "PKGSIZE":
					pkginfo.size = long(value)
				elif name == "FILES":
					pkginfo.files = value
				elif name == "FILELIST":
					pkginfo.filelist = value
		finally:
			f.close()
		return pkginfo

	def getlastrecipeversion(pn):
		try:
			histfile = os.path.join(pkghistdir, "latest")
			return readRecipeInfo(pn, histfile)
		except EnvironmentError:
			return None

	def getlastpkgversion(pkg):
		try:
			histfile = os.path.join(pkghistdir, pkg, "latest")
			return readPackageInfo(pkg, histfile)
		except EnvironmentError:
			return None

	def squashspaces(string):
		return re.sub("\s+", " ", string)

	pn = d.getVar('PN', True)
	pe = d.getVar('PE', True) or "0"
	pv = d.getVar('PV', True)
	pr = d.getVar('PR', True)
	packages = squashspaces(d.getVar('PACKAGES', True))

	rcpinfo = RecipeInfo(pn)
	rcpinfo.pe = pe
	rcpinfo.pv = pv
	rcpinfo.pr = pr
	rcpinfo.depends = squashspaces(d.getVar('DEPENDS', True) or "")
	rcpinfo.packages = packages
	write_recipehistory(rcpinfo, d)
	write_latestlink(None, pe, pv, pr, d)

	# Apparently the version can be different on a per-package basis (see Python)
	pkgdest = d.getVar('PKGDEST', True)
	for pkg in packages.split():
		pe = getpkgvar(pkg, 'PE') or "0"
		pv = getpkgvar(pkg, 'PV')
		pr = getpkgvar(pkg, 'PR')
		#
		# Find out what the last version was
		# Make sure the version did not decrease
		#
		lastversion = getlastpkgversion(pkg)
		if lastversion:
			last_pe = lastversion.pe
			last_pv = lastversion.pv
			last_pr = lastversion.pr
			r = bb.utils.vercmp((pe, pv, pr), (last_pe, last_pv, last_pr))
			if r < 0:
				bb.fatal("Package version for package %s went backwards which would break package feeds from (%s:%s-%s to %s:%s-%s)" % (pkg, last_pe, last_pv, last_pr, pe, pv, pr))

		pkginfo = PackageInfo(pkg)
		pkginfo.pe = pe
		pkginfo.pv = pv
		pkginfo.pr = pr
		pkginfo.rdepends = squashspaces(getpkgvar(pkg, 'RDEPENDS') or "")
		pkginfo.rrecommends = squashspaces(getpkgvar(pkg, 'RRECOMMENDS') or "")
		pkginfo.files = squashspaces(getpkgvar(pkg, 'FILES') or "")

		# Gather information about packaged files
		pkgdestpkg = os.path.join(pkgdest, pkg)
		filelist = []
		pkginfo.size = 0
		for root, dirs, files in os.walk(pkgdestpkg):
			relpth = os.path.relpath(root, pkgdestpkg)
			for f in files:
				fstat = os.lstat(os.path.join(root, f))
				pkginfo.size += fstat.st_size
				filelist.append(os.sep + os.path.join(relpth, f))
		pkginfo.filelist = " ".join(filelist)

		write_pkghistory(pkginfo, d)

		if lastversion:
			check_pkghistory(pkginfo, lastversion)

		write_latestlink(pkg, pe, pv, pr, d)
}


def check_pkghistory(pkginfo, lastversion):

	bb.debug(2, "Checking package history")
	# RDEPENDS removed?
	# PKG changed?
	# Each file list of each package for file removals?


def write_recipehistory(rcpinfo, d):
	bb.debug(2, "Writing recipe history")

	pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)

	if not os.path.exists(pkghistdir):
		os.makedirs(pkghistdir)

	verfile = os.path.join(pkghistdir, "%s:%s-%s" % (rcpinfo.pe, rcpinfo.pv, rcpinfo.pr))
	f = open(verfile, "w")
	try:
		if rcpinfo.pe != "0":
			f.write("PE = %s\n" %  rcpinfo.pe)
		f.write("PV = %s\n" %  rcpinfo.pv)
		f.write("PR = %s\n" %  rcpinfo.pr)
		f.write("DEPENDS = %s\n" %  rcpinfo.depends)
		f.write("PACKAGES = %s\n" %  rcpinfo.packages)
	finally:
		f.close()


def write_pkghistory(pkginfo, d):
	bb.debug(2, "Writing package history")

	pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)

	verpath = os.path.join(pkghistdir, pkginfo.name)
	if not os.path.exists(verpath):
		os.makedirs(verpath)

	verfile = os.path.join(verpath, "%s:%s-%s" % (pkginfo.pe, pkginfo.pv, pkginfo.pr))
	f = open(verfile, "w")
	try:
		if pkginfo.pe != "0":
			f.write("PE = %s\n" %  pkginfo.pe)
		f.write("PV = %s\n" %  pkginfo.pv)
		f.write("PR = %s\n" %  pkginfo.pr)
		f.write("RDEPENDS = %s\n" %  pkginfo.rdepends)
		f.write("RRECOMMENDS = %s\n" %  pkginfo.rrecommends)
		f.write("PKGSIZE = %d\n" %  pkginfo.size)
		f.write("FILES = %s\n" %  pkginfo.files)
		f.write("FILELIST = %s\n" %  pkginfo.filelist)
	finally:
		f.close()


def write_latestlink(pkg, pe, pv, pr, d):
	import shutil

	pkghistdir = d.getVar('BUILDHISTORY_DIR_PACKAGE', True)

	def rm_link(path):
		try:
			os.unlink(path)
		except OSError:
			return

	if pkg:
		filedir = os.path.join(pkghistdir, pkg)
	else:
		filedir = pkghistdir
	rm_link(os.path.join(filedir, "latest"))
	shutil.copy(os.path.join(filedir, "%s:%s-%s" % (pe, pv, pr)), os.path.join(filedir, "latest"))


buildhistory_get_image_installed() {
	# Anything requiring the use of the packaging system should be done in here
	# in case the packaging files are going to be removed for this image

	mkdir -p ${BUILDHISTORY_DIR_IMAGE}

	# Get list of installed packages
	list_installed_packages | sort > ${BUILDHISTORY_DIR_IMAGE}/installed-package-names.txt
	INSTALLED_PKGS=`cat ${BUILDHISTORY_DIR_IMAGE}/installed-package-names.txt`

	# Produce installed package file and size lists and dependency graph
	echo -n > ${BUILDHISTORY_DIR_IMAGE}/installed-packages.txt
	echo -n > ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp
	echo -e "digraph depends {\n    node [shape=plaintext]" > ${BUILDHISTORY_DIR_IMAGE}/depends.dot
	for pkg in $INSTALLED_PKGS; do
		pkgfile=`get_package_filename $pkg`
		echo `basename $pkgfile` >> ${BUILDHISTORY_DIR_IMAGE}/installed-packages.txt
		if [ -f $pkgfile ] ; then
			pkgsize=`du -k $pkgfile | head -n1 | awk '{ print $1 }'`
			echo $pkgsize $pkg >> ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp
		fi

		deps=`list_package_depends $pkg`
		for dep in $deps ; do
			echo "$pkg OPP $dep;" | sed -e 's:-:_:g' -e 's:\.:_:g' -e 's:+::g' | sed 's:OPP:->:g' >> ${BUILDHISTORY_DIR_IMAGE}/depends.dot
		done

		recs=`list_package_recommends $pkg`
		for rec in $recs ; do
			echo "$pkg OPP $rec [style=dotted];" | sed -e 's:-:_:g' -e 's:\.:_:g' -e 's:+::g' | sed 's:OPP:->:g' >> ${BUILDHISTORY_DIR_IMAGE}/depends.dot
		done
	done
	echo "}" >>  ${BUILDHISTORY_DIR_IMAGE}/depends.dot

	cat ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp | sort -n -r | awk '{print $1 "\tKiB " $2}' > ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.txt
	rm ${BUILDHISTORY_DIR_IMAGE}/installed-package-sizes.tmp

	# Produce some cut-down graphs (for readability)
	grep -v kernel_image ${BUILDHISTORY_DIR_IMAGE}/depends.dot | grep -v kernel_2 | grep -v kernel_3 > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel.dot
	grep -v libc6 ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel.dot | grep -v libgcc > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc.dot
	grep -v update_ ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc.dot > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate.dot
	grep -v kernel_module ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate.dot > ${BUILDHISTORY_DIR_IMAGE}/depends-nokernel-nolibc-noupdate-nomodules.dot

	# Workaround for broken shell function dependencies
	if false ; then
		get_package_filename
		list_package_depends
		list_package_recommends
	fi
}

buildhistory_get_imageinfo() {
	# List the files in the image, but exclude date/time etc.
	# This awk script is somewhat messy, but handles where the size is not printed for device files under pseudo
	find ${IMAGE_ROOTFS} -ls | awk '{ if ( $7 ~ /[0-9]/ ) printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, $7, $11, $12, $13 ; else printf "%s %10-s %10-s %10s %s %s %s\n", $3, $5, $6, 0, $10, $11, $12 }' > ${BUILDHISTORY_DIR_IMAGE}/files-in-image.txt

	# Add some configuration information
	echo "${MACHINE}: ${IMAGE_BASENAME} configured for ${DISTRO} ${DISTRO_VERSION}" > ${BUILDHISTORY_DIR_IMAGE}/build-id
	echo "${@buildhistory_get_layers(d)}" >> ${BUILDHISTORY_DIR_IMAGE}/build-id
}

# By prepending we get in before the removal of packaging files
ROOTFS_POSTPROCESS_COMMAND =+ "buildhistory_get_image_installed ; "

IMAGE_POSTPROCESS_COMMAND += " buildhistory_get_imageinfo ; "

def buildhistory_get_layers(d):
	layertext = "Configured metadata layers:\n%s\n" % '\n'.join(get_layers_branch_rev(d))
	return layertext


buildhistory_commit() {
	( cd ${BUILDHISTORY_DIR}/
		git add ${BUILDHISTORY_DIR}/*
		git commit ${BUILDHISTORY_DIR}/ -m "Build ${BUILDNAME} for machine ${MACHINE} configured for ${DISTRO} ${DISTRO_VERSION}" --author "${BUILDHISTORY_COMMIT_AUTHOR}" > /dev/null
		if [ "${BUILDHISTORY_PUSH_REPO}" != "" ] ; then
			git push -q ${BUILDHISTORY_PUSH_REPO}
		fi) || true
}

python buildhistory_eventhandler() {
	import bb.build
	import bb.event

	if isinstance(e, bb.event.BuildCompleted):
		if e.data.getVar("BUILDHISTORY_COMMIT", True) == "1":
			bb.build.exec_func("buildhistory_commit", e.data)
}

addhandler buildhistory_eventhandler
