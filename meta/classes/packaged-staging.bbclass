#
# Populate builds using prebuilt packages where possible to speed up builds
# and allow staging to be reconstructed.
#
# To use it add that line to conf/local.conf:
#
# INHERIT += "packaged-staging"
#


#
# bitbake.conf set PSTAGING_ACTIVE = "0", this class sets to "1" if we're active
# 
PSTAGE_PKGVERSION = "${PV}-${PR}"
PSTAGE_PKGARCH    = "${BUILD_SYS}"
PSTAGE_EXTRAPATH  ?= ""
PSTAGE_PKGPATH    = "${DISTRO}${PSTAGE_EXTRAPATH}"
PSTAGE_PKGPN      = "${@bb.data.expand('staging-${PN}-${MULTIMACH_ARCH}${TARGET_VENDOR}-${TARGET_OS}', d).replace('_', '-')}"
PSTAGE_PKGNAME 	  = "${PSTAGE_PKGPN}_${PSTAGE_PKGVERSION}_${PSTAGE_PKGARCH}.ipk"
PSTAGE_PKG        = "${DEPLOY_DIR_PSTAGE}/${PSTAGE_PKGPATH}/${PSTAGE_PKGNAME}"

# multimachine.bbclass will override this but add a default in case we're not using it
MULTIMACH_ARCH ?= "${PACKAGE_ARCH}"

PSTAGE_NATIVEDEPENDS = "\
    pkgconfig-native \
    autoconf-native \
    automake-native \
    curl-native \
    zlib-native \
    libtool-native \
    gnu-config-native \
    shasum-native \
    libtool-native \
    automake-native \
    update-alternatives-cworth-native \
    ipkg-utils-native \
    opkg-native \
    m4-native \
    quilt-native \
    stagemanager-native \
    "

BB_STAMP_WHITELIST = "${PSTAGE_NATIVEDEPENDS}"

python () {
    import bb
    pstage_allowed = True

    # These classes encode staging paths into the binary data so can only be
    # reused if the path doesn't change/
    if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('sdk', d):
        path = bb.data.getVar('PSTAGE_PKGPATH', d, 1)
        path = path + bb.data.getVar('TMPDIR', d, 1).replace('/', '-')
        bb.data.setVar('PSTAGE_PKGPATH', path, d)

    # PSTAGE_NATIVEDEPENDS lists the packages we need before we can use packaged 
    # staging. There will always be some packages we depend on.
    if bb.data.inherits_class('native', d):
        pn = bb.data.getVar('PN', d, True)
        nativedeps = bb.data.getVar('PSTAGE_NATIVEDEPENDS', d, True).split()
        if pn in nativedeps:
            pstage_allowed = False

    # Images aren't of interest to us
    if bb.data.inherits_class('image', d):
        pstage_allowed = False

    # Add task dependencies if we're active, otherwise mark packaged staging
    # as inactive.
    if pstage_allowed:
        deps = bb.data.getVarFlag('do_populate_staging', 'depends', d) or ""
        deps += " stagemanager-native:do_populate_staging"
        bb.data.setVarFlag('do_populate_staging', 'depends', deps, d)

        deps = bb.data.getVarFlag('do_setscene', 'depends', d) or ""
        deps += " opkg-native:do_populate_staging ipkg-utils-native:do_populate_staging"
        bb.data.setVarFlag('do_setscene', 'depends', deps, d)
        bb.data.setVar("PSTAGING_ACTIVE", "1", d)
    else:
        bb.data.setVar("PSTAGING_ACTIVE", "0", d)
}

DEPLOY_DIR_PSTAGE 	= "${DEPLOY_DIR}/pstage"
PSTAGE_MACHCONFIG       = "${DEPLOY_DIR_PSTAGE}/opkg.conf"

PSTAGE_BUILD_CMD        = "${IPKGBUILDCMD}"
PSTAGE_INSTALL_CMD      = "opkg-cl install -force-depends -f ${PSTAGE_MACHCONFIG} -o ${TMPDIR}"
PSTAGE_UPDATE_CMD	= "opkg-cl update -f ${PSTAGE_MACHCONFIG} -o ${TMPDIR}"
PSTAGE_REMOVE_CMD       = "opkg-cl remove -force-depends -f ${PSTAGE_MACHCONFIG} -o ${TMPDIR}"
PSTAGE_LIST_CMD		= "opkg-cl list_installed -f ${PSTAGE_MACHCONFIG} -o ${TMPDIR}"

PSTAGE_TMPDIR_STAGE     = "${WORKDIR}/staging-pkg"

def pstage_manualclean(srcname, destvarname, d):
	import os, bb

	src = os.path.join(bb.data.getVar('PSTAGE_TMPDIR_STAGE', d, True), srcname)
	dest = bb.data.getVar(destvarname, d, True)

	for walkroot, dirs, files in os.walk(src):
		for file in files:
			filepath = os.path.join(walkroot, file).replace(src, dest)
			bb.note("rm %s" % filepath)
			os.system("rm %s" % filepath)

def pstage_cleanpackage(pkgname, d):
	import os, bb

        path = bb.data.getVar("PATH", d, 1)
	list_cmd = bb.data.getVar("PSTAGE_LIST_CMD", d, True)

	bb.note("Checking if staging package installed")
	lf = bb.utils.lockfile(bb.data.expand("${STAGING_DIR}/staging.lock", d))
	ret = os.system("PATH=\"%s\" %s | grep %s" % (path, list_cmd, pkgname))
	if ret == 0:
		bb.note("Yes. Uninstalling package from staging...")
		removecmd = bb.data.getVar("PSTAGE_REMOVE_CMD", d, 1)
		ret = os.system("PATH=\"%s\" %s %s" % (path, removecmd, pkgname))
		if ret != 0:
			bb.note("Failure removing staging package")
	else:
		bb.note("No. Manually removing any installed files")
		pstage_manualclean("staging", "STAGING_DIR", d)
		pstage_manualclean("cross", "CROSS_DIR", d)
		pstage_manualclean("deploy", "DEPLOY_DIR", d)

	bb.utils.unlockfile(lf)

do_clean_prepend() {
        """
        Clear the build and temp directories
        """

	removepkg = bb.data.expand("${PSTAGE_PKGPN}", d)
	pstage_cleanpackage(removepkg, d)

        stagepkg = bb.data.expand("${PSTAGE_PKG}", d)
        bb.note("Removing staging package %s" % stagepkg)
        os.system('rm -rf ' + stagepkg)
}

staging_helper () {
	# Assemble appropriate opkg.conf
	conffile=${PSTAGE_MACHCONFIG}
	mkdir -p ${DEPLOY_DIR_PSTAGE}/pstaging_lists
	if [ ! -e $conffile ]; then
		ipkgarchs="${BUILD_SYS}"
		priority=1
		for arch in $ipkgarchs; do
			echo "arch $arch $priority" >> $conffile
			priority=$(expr $priority + 5)
		done
	fi
}

PSTAGE_TASKS_COVERED = "fetch unpack munge patch configure qa_configure rig_locales compile sizecheck install deploy package populate_staging package_write_deb package_write_ipk package_write package_stage qa_staging"

SCENEFUNCS += "packagestage_scenefunc"

python packagestage_scenefunc () {
    import os

    if bb.data.getVar("PSTAGING_ACTIVE", d, 1) == "0":
        return

    removepkg = bb.data.expand("${PSTAGE_PKGPN}", d)
    pstage_cleanpackage(removepkg, d)

    stagepkg = bb.data.expand("${PSTAGE_PKG}", d)

    if os.path.exists(stagepkg):
        bb.note("Following speedup\n")
        path = bb.data.getVar("PATH", d, 1)
        installcmd = bb.data.getVar("PSTAGE_INSTALL_CMD", d, 1)

        bb.build.exec_func("staging_helper", d)

        bb.debug(1, "Staging stuff already packaged, using that instead")
        lf = bb.utils.lockfile(bb.data.expand("${STAGING_DIR}/staging.lock", d))
        ret = os.system("PATH=\"%s\" %s %s" % (path, installcmd, stagepkg))
        bb.utils.unlockfile(lf)
        if ret != 0:
            bb.note("Failure installing prestage package")

        bb.build.make_stamp("do_stage_package_populated", d)

}
packagestage_scenefunc[cleandirs] = "${PSTAGE_TMPDIR_STAGE}"
packagestage_scenefunc[dirs] = "${STAGING_DIR}"

addhandler packagedstage_stampfixing_eventhandler
python packagedstage_stampfixing_eventhandler() {
    from bb.event import getName
    import os

    if getName(e) == "StampUpdate":
        taskscovered = bb.data.getVar("PSTAGE_TASKS_COVERED", e.data, 1).split()
        for (fn, task) in e.targets:
            # strip off 'do_'
            task = task[3:]
            if task in taskscovered:
                stamp = "%s.do_stage_package_populated" % e.stampPrefix[fn]
                if os.path.exists(stamp):
                    # We're targetting a task which was skipped with packaged staging
                    # so we need to remove the autogenerated stamps.
                    for task in taskscovered:
                        dir = "%s.do_%s" % (e.stampPrefix[fn], task)
                        os.system('rm -f ' + dir)
                    os.system('rm -f ' + stamp)

    return NotHandled
}

populate_staging_preamble () {
	if [ "$PSTAGING_ACTIVE" = "1" ]; then
		stage-manager -p ${STAGING_DIR} -c ${DEPLOY_DIR_PSTAGE}/stamp-cache-staging -u || true
		stage-manager -p ${CROSS_DIR} -c ${DEPLOY_DIR_PSTAGE}/stamp-cache-cross -u || true
	fi
}

populate_staging_postamble () {
	if [ "$PSTAGING_ACTIVE" = "1" ]; then
		# list the packages currently installed in staging
		# ${PSTAGE_LIST_CMD} | awk '{print $1}' > ${DEPLOY_DIR_PSTAGE}/installed-list         

		set +e
		stage-manager -p ${STAGING_DIR} -c ${DEPLOY_DIR_PSTAGE}/stamp-cache-staging -u -d ${PSTAGE_TMPDIR_STAGE}/staging
		stage-manager -p ${CROSS_DIR} -c ${DEPLOY_DIR_PSTAGE}/stamp-cache-cross -u -d ${PSTAGE_TMPDIR_STAGE}/cross
		set -e
	fi
}

do_populate_staging[lockfiles] = "${STAGING_DIR}/staging.lock"
do_populate_staging[dirs] =+ "${DEPLOY_DIR_PSTAGE}"
python do_populate_staging_prepend() {
    bb.build.exec_func("populate_staging_preamble", d)
}

python do_populate_staging_append() {
    bb.build.exec_func("populate_staging_postamble", d)
}


staging_packager () {

	mkdir -p ${PSTAGE_TMPDIR_STAGE}/CONTROL
	mkdir -p ${DEPLOY_DIR_PSTAGE}/${PSTAGE_PKGPATH}

	echo "Package: ${PSTAGE_PKGPN}"         >  ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Version: ${PSTAGE_PKGVERSION}"    >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Description: ${DESCRIPTION}"      >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Section: ${SECTION}"              >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Priority: Optional"               >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Maintainer: ${MAINTAINER}"        >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Architecture: ${PSTAGE_PKGARCH}"  >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	
	# Protect against empty SRC_URI
	if [ "${SRC_URI}" != "" ] ; then		
		echo "Source: ${SRC_URI}"               >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	else
		echo "Source: OpenEmbedded"               >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	fi
        
	${PSTAGE_BUILD_CMD} ${PSTAGE_TMPDIR_STAGE} ${DEPLOY_DIR_PSTAGE}/${PSTAGE_PKGPATH}
	${PSTAGE_INSTALL_CMD} ${PSTAGE_PKG}
}

staging_package_installer () {
	#${PSTAGE_INSTALL_CMD} ${PSTAGE_PKG}

	STATUSFILE=${TMPDIR}${layout_libdir}/opkg/status
	echo "Package: ${PSTAGE_PKGPN}"        >> $STATUSFILE
	echo "Version: ${PSTAGE_PKGVERSION}"   >> $STATUSFILE
	echo "Status: install user installed"  >> $STATUSFILE
	echo "Architecture: ${PSTAGE_PKGARCH}" >> $STATUSFILE
	echo "" >> $STATUSFILE
}

python do_package_stage () {
    if bb.data.getVar("PSTAGING_ACTIVE", d, 1) != "1":
        return

    #
    # Handle deploy/ packages
    #
    bb.build.exec_func("read_subpackage_metadata", d)
    stagepath = bb.data.getVar("PSTAGE_TMPDIR_STAGE", d, 1)
    tmpdir = bb.data.getVar("TMPDIR", d, True)
    packages = (bb.data.getVar('PACKAGES', d, 1) or "").split()
    if len(packages) > 0:
        if bb.data.inherits_class('package_ipk', d):
            ipkpath = bb.data.getVar('DEPLOY_DIR_IPK', d, True).replace(tmpdir, stagepath)
        if bb.data.inherits_class('package_deb', d):
            debpath = bb.data.getVar('DEPLOY_DIR_DEB', d, True).replace(tmpdir, stagepath)

        for pkg in packages:
            pkgname = bb.data.getVar('PKG_%s' % pkg, d, 1)
            if not pkgname:
                pkgname = pkg
            arch = bb.data.getVar('PACKAGE_ARCH_%s' % pkg, d, 1)
            if not arch:
                arch = bb.data.getVar('PACKAGE_ARCH', d, 1)
            pr = bb.data.getVar('PR_%s' % pkg, d, 1)
            if not pr:
                pr = bb.data.getVar('PR', d, 1)
            if not packaged(pkg, d):
                continue
            if bb.data.inherits_class('package_ipk', d):
                srcname = bb.data.expand(pkgname + "_${PV}-" + pr + "_" + arch + ".ipk", d)
                srcfile = bb.data.expand("${DEPLOY_DIR_IPK}/" + arch + "/" + srcname, d)
                if os.path.exists(srcfile):
                    destpath = ipkpath + "/" + arch + "/"
                    bb.mkdirhier(destpath)
                    bb.copyfile(srcfile, destpath + srcname)

            if bb.data.inherits_class('package_deb', d):
                if arch == 'all':
                    srcname = bb.data.expand(pkgname + "_${PV}-" + pr + "_all.deb", d)
                else:	
                    srcname = bb.data.expand(pkgname + "_${PV}-" + pr + "_${DPKG_ARCH}.deb", d)
                srcfile = bb.data.expand("${DEPLOY_DIR_DEB}/" + arch + "/" + srcname, d)
                if os.path.exists(srcfile):
                    destpath = debpath + "/" + arch + "/" 
                    bb.mkdirhier(destpath)
                    bb.copyfile(srcfile, destpath + srcname)

    #
    # Handle stamps/ files
    #
    stampfn = bb.data.getVar("STAMP", d, True)
    destdir = os.path.dirname(stampfn.replace(tmpdir, stagepath))
    bb.mkdirhier(destdir)
    # We need to include the package_stage stamp in the staging package so create one
    bb.build.make_stamp("do_package_stage", d)
    os.system("cp -dpR %s.do_* %s/" % (stampfn, destdir))

    bb.build.exec_func("staging_helper", d)
    bb.build.exec_func("staging_packager", d)
    lf = bb.utils.lockfile(bb.data.expand("${STAGING_DIR}/staging.lock", d))
    bb.build.exec_func("staging_package_installer", d)
    bb.utils.unlockfile(lf)
}

#
# Note an assumption here is that do_deploy runs before do_package_write/do_populate_staging
#
addtask package_stage after do_package_write do_populate_staging before do_build

do_package_stage_all () {
	:
}
do_package_stage_all[recrdeptask] = "do_package_stage"
addtask package_stage_all after do_package_stage before do_build




