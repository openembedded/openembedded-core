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
PSTAGE_PKGARCH    = "${TARGET_ARCH}"
PSTAGE_EXTRAPATH  ?= ""
PSTAGE_PKGPATH    = "${DISTRO}/${OELAYOUT_ABI}${PSTAGE_EXTRAPATH}"
PSTAGE_PKGPN      = "${@bb.data.expand('staging-${PN}-${MULTIMACH_ARCH}${TARGET_VENDOR}-${TARGET_OS}', d).replace('_', '-')}"
PSTAGE_PKGNAME    = "${PSTAGE_PKGPN}_${PSTAGE_PKGVERSION}_${PSTAGE_PKGARCH}.ipk"
PSTAGE_PKG        = "${PSTAGE_DIR}/${PSTAGE_PKGPATH}/${PSTAGE_PKGNAME}"
PSTAGE_WORKDIR   = "${TMPDIR}/pstage"
PSTAGE_SCAN_CMD ?= "find ${PSTAGE_TMPDIR_STAGE} \( -name "*.la" -o -name "*-config" \) -type f"

PSTAGE_NATIVEDEPENDS = "\
    shasum-native \
    stagemanager-native \
    "

BB_STAMP_WHITELIST = "${PSTAGE_NATIVEDEPENDS}"

python () {
    if bb.data.inherits_class('native', d):
            bb.data.setVar('PSTAGE_PKGARCH', bb.data.getVar('BUILD_ARCH', d), d)
    elif bb.data.inherits_class('cross', d) or bb.data.inherits_class('crosssdk', d):
            bb.data.setVar('PSTAGE_PKGARCH', bb.data.expand("${BUILD_ARCH}_${TARGET_ARCH}", d), d)

    pstage_allowed = True

    # These classes encode staging paths into the binary data so can only be
    # reused if the path doesn't change/
    if bb.data.inherits_class('native', d) or bb.data.inherits_class('cross', d) or bb.data.inherits_class('sdk', d) or bb.data.inherits_class('crosssdk', d):
        path = bb.data.getVar('PSTAGE_PKGPATH', d, 1)
        path = path + bb.data.getVar('TMPDIR', d, 1).replace('/', '-')
        bb.data.setVar('PSTAGE_PKGPATH', path, d)
        scan_cmd = "grep -Irl ${STAGING_DIR} ${PSTAGE_TMPDIR_STAGE}"
        bb.data.setVar('PSTAGE_SCAN_CMD', scan_cmd, d)

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

    if bb.data.getVar('PSTAGING_DISABLED', d, True) == "1":
        pstage_allowed = False

    # Add task dependencies if we're active, otherwise mark packaged staging
    # as inactive.
    if pstage_allowed:
        deps = bb.data.getVarFlag('do_setscene', 'depends', d) or ""
        deps += " stagemanager-native:do_populate_sysroot"
        bb.data.setVarFlag('do_setscene', 'depends', deps, d)

        policy = bb.data.getVar("BB_STAMP_POLICY", d, True)
        if policy == "whitelist" or policy == "full":
           deps = bb.data.getVarFlag('do_setscene', 'recrdeptask', d) or ""
           deps += " do_setscene"
           bb.data.setVarFlag('do_setscene', 'recrdeptask', deps, d)

        bb.data.setVar("PSTAGING_ACTIVE", "1", d)
    else:
        bb.data.setVar("PSTAGING_ACTIVE", "0", d)
}

PSTAGE_MACHCONFIG   = "${PSTAGE_WORKDIR}/${TARGET_ARCH}-opkg.conf"

PSTAGE_PKGMANAGER = "stage-manager-ipkg"

PSTAGE_BUILD_CMD        = "stage-manager-ipkg-build -o 0 -g 0"
PSTAGE_INSTALL_CMD      = "${PSTAGE_PKGMANAGER} -f ${PSTAGE_MACHCONFIG} -force-depends -o ${TMPDIR} install"
PSTAGE_UPDATE_CMD       = "${PSTAGE_PKGMANAGER} -f ${PSTAGE_MACHCONFIG} -o ${TMPDIR} update"
PSTAGE_REMOVE_CMD       = "${PSTAGE_PKGMANAGER} -f ${PSTAGE_MACHCONFIG} -force-depends -o ${TMPDIR} remove"
PSTAGE_LIST_CMD         = "${PSTAGE_PKGMANAGER} -f ${PSTAGE_MACHCONFIG} -o ${TMPDIR} list_installed"

PSTAGE_TMPDIR_STAGE     = "${WORKDIR}/staging-pkg"

def pstage_manualclean(srcname, destvarname, d):
	src = os.path.join(bb.data.getVar('PSTAGE_TMPDIR_STAGE', d, True), srcname)
	dest = bb.data.getVar(destvarname, d, True)

	for walkroot, dirs, files in os.walk(src):
		for file in files:
			# Avoid breaking the held lock
			if (file == "staging.lock"):
				continue
			filepath = os.path.join(walkroot, file).replace(src, dest)
			os.system("rm %s 2> /dev/null" % filepath)

def pstage_set_pkgmanager(d):
    path = bb.data.getVar("PATH", d, 1)
    pkgmanager = bb.which(path, 'opkg-cl')
    if pkgmanager == "":
        pkgmanager = bb.which(path, 'ipkg-cl')
    if pkgmanager != "":
        bb.data.setVar("PSTAGE_PKGMANAGER", pkgmanager, d)


def pstage_cleanpackage(pkgname, d):
	path = bb.data.getVar("PATH", d, 1)
	pstage_set_pkgmanager(d)
	list_cmd = bb.data.getVar("PSTAGE_LIST_CMD", d, True)

	bb.note("Checking if staging package installed")
	lf = bb.utils.lockfile(bb.data.expand("${SYSROOT_LOCK}", d))
	ret = os.system("PATH=\"%s\" %s | grep %s" % (path, list_cmd, pkgname))
	if ret == 0:
		bb.note("Yes. Uninstalling package from staging...")
		removecmd = bb.data.getVar("PSTAGE_REMOVE_CMD", d, 1)
		ret = os.system("PATH=\"%s\" %s %s" % (path, removecmd, pkgname))
		if ret != 0:
			bb.note("Failure removing staging package")
	else:
		bb.note("No. Manually removing any installed files")
		pstage_manualclean("sysroots", "STAGING_DIR", d)
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
        # Add a wildcard to the end of stagepkg to also get its md5
        # if it's a fetched package
	os.system('rm -rf ' + stagepkg + '*')
}

staging_helper () {
	# Assemble appropriate opkg.conf for the target arch
	conffile=${PSTAGE_MACHCONFIG}
	mkdir -p ${PSTAGE_WORKDIR}/pstaging_lists
	if [ ! -e $conffile ]; then
		ipkgarchs="${TARGET_ARCH} ${BUILD_ARCH} ${BUILD_ARCH}_${TARGET_ARCH}"
		priority=1
		for arch in $ipkgarchs; do
			echo "arch $arch $priority" >> $conffile
			priority=$(expr $priority + 5)
		done
		echo "dest root /" >> $conffile
	fi
	if [ ! -e ${TMPDIR}${libdir_native}/opkg/info/ ]; then
		mkdir -p ${TMPDIR}${libdir_native}/opkg/info/
	fi
 	if [ ! -e ${TMPDIR}${libdir_native}/ipkg/ ]; then
		ln -sf opkg/ ${TMPDIR}${libdir_native}/ipkg || true
	fi
}

def staging_fetch(stagepkg, d):
    import bb.fetch

    # only try and fetch if the user has configured a mirror
    if bb.data.getVar('PSTAGE_MIRROR', d) != "":
        # Copy the data object and override DL_DIR and SRC_URI
        pd = d.createCopy()
        dldir = bb.data.expand("${PSTAGE_DIR}/${PSTAGE_PKGPATH}", pd)
        mirror = bb.data.expand("${PSTAGE_MIRROR}/${PSTAGE_PKGPATH}/", pd)
        srcuri = mirror + os.path.basename(stagepkg)
        bb.data.setVar('DL_DIR', dldir, pd)
        bb.data.setVar('SRC_URI', srcuri, pd)

        # Try a fetch from the pstage mirror, if it fails just return and
        # we will build the package
        try:
            bb.fetch.init([srcuri], pd)
            bb.fetch.go(pd, [srcuri])
        except:
            return

PSTAGE_TASKS_COVERED = "fetch unpack munge patch configure qa_configure rig_locales compile sizecheck install deploy package populate_sysroot package_write_deb package_write_ipk package_write package_stage qa_staging"

SCENEFUNCS += "packagestage_scenefunc"

python packagestage_scenefunc () {
    if bb.data.getVar("PSTAGING_ACTIVE", d, 1) == "0":
        return

    bb.build.exec_func("staging_helper", d)

    removepkg = bb.data.expand("${PSTAGE_PKGPN}", d)

    pstage_cleanpackage(removepkg, d)

    stagepkg = bb.data.expand("${PSTAGE_PKG}", d)
    if not os.path.exists(stagepkg):
        staging_fetch(stagepkg, d)

    if os.path.exists(stagepkg):
        path = bb.data.getVar("PATH", d, 1)
        pstage_set_pkgmanager(d)
        file = bb.data.getVar("FILE", d, True)
        bb.debug(2, "Packaged staging active for %s\n" % file)

        #
        # Install the staging package somewhere temporarily so we can extract the stamp files
        #
        bb.mkdirhier(bb.data.expand("${WORKDIR}/tstage/${libdir_native}/opkg/info/ ", d))
        cmd = bb.data.expand("${PSTAGE_PKGMANAGER} -f ${PSTAGE_MACHCONFIG} -force-depends -o ${WORKDIR}/tstage install", d)
        ret = os.system("PATH=\"%s\" %s %s" % (path, cmd, stagepkg))
        if ret != 0:
            bb.fatal("Couldn't install the staging package to a temp directory")

        #
        # Copy the stamp files into the main stamps directoy
        #
        cmd = bb.data.expand("cp -dpR ${WORKDIR}/tstage/stamps/* ${TMPDIR}/stamps/", d)
        ret = os.system(cmd)
        if ret != 0:
            bb.fatal("Couldn't copy the staging package stamp files")

        #
        # Iterate over the stamps seeing if they're valid. If we find any that
        # are invalid or the task wasn't in the taskgraph, assume caution and 
        # do a rebuild.
        #
        # FIXME - some tasks are safe to ignore in the task graph. e.g. package_write_*
        stageok = True
        taskscovered = bb.data.getVar("PSTAGE_TASKS_COVERED", d, True).split()
        stamp = bb.data.getVar("STAMP", d, True)
        for task in taskscovered:
            task = 'do_' + task
            stampfn = "%s.%s" % (stamp, task)
            bb.debug(1, "Checking %s" % (stampfn))
            if os.path.exists(stampfn):
                stageok = bb.runqueue.check_stamp_fn(file, task, d)
                bb.debug(1, "Result %s" % (stageok))
                if not stageok:
                    break

        # Remove the stamps and files we added above
        # FIXME - we should really only remove the stamps we added
        os.system('rm -f ' + stamp + '.*')
        os.system(bb.data.expand("rm -rf ${WORKDIR}/tstage", d))

        if stageok:
            bb.note("Staging package found, using it for %s." % file)
            installcmd = bb.data.getVar("PSTAGE_INSTALL_CMD", d, 1)
            lf = bb.utils.lockfile(bb.data.expand("${SYSROOT_LOCK}", d))
            ret = os.system("PATH=\"%s\" %s %s" % (path, installcmd, stagepkg))
            bb.utils.unlockfile(lf)
            if ret != 0:
                bb.note("Failure installing prestage package")

            bb.build.exec_func("staging_package_libtoolhack", d)

            bb.build.make_stamp("do_stage_package_populated", d)
        else:
            bb.note("Staging package found but invalid for %s" % file)

}
packagestage_scenefunc[cleandirs] = "${PSTAGE_TMPDIR_STAGE}"
packagestage_scenefunc[dirs] = "${STAGING_DIR}"

addhandler packagedstage_stampfixing_eventhandler
python packagedstage_stampfixing_eventhandler() {
    if bb.event.getName(e) == "StampUpdate":
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

populate_sysroot_preamble () {
	if [ "$PSTAGING_ACTIVE" = "1" ]; then
		stage-manager -p ${STAGING_DIR} -c ${PSTAGE_WORKDIR}/stamp-cache-staging -u || true
	fi
}

populate_sysroot_postamble () {
	if [ "$PSTAGING_ACTIVE" = "1" ]; then
		# list the packages currently installed in staging
		# ${PSTAGE_LIST_CMD} | awk '{print $1}' > ${PSTAGE_WORKDIR}/installed-list

		# exitcode == 5 is ok, it means the files change
		set +e
		stage-manager -p ${STAGING_DIR} -c ${PSTAGE_WORKDIR}/stamp-cache-staging -u -d ${PSTAGE_TMPDIR_STAGE}/sysroots
		exitcode=$?
		if [ "$exitcode" != "5" -a "$exitcode" != "0" ]; then
			exit $exitcode
		fi
		if [ "$exitcode" != "5" -a "$exitcode" != "0" ]; then
			exit $exitcode
		fi
		set -e
	fi
}

packagedstaging_fastpath () {
	if [ "$PSTAGING_ACTIVE" = "1" ]; then
		mkdir -p ${PSTAGE_TMPDIR_STAGE}/sysroots/
		cp -fpPR ${SYSROOT_DESTDIR}/${STAGING_DIR}/* ${PSTAGE_TMPDIR_STAGE}/sysroots/ || /bin/true
	fi
}

do_populate_sysroot[dirs] =+ "${PSTAGE_DIR}"
python populate_sysroot_prehook() {
    bb.build.exec_func("populate_sysroot_preamble", d)
}

python populate_sysroot_posthook() {
    bb.build.exec_func("populate_sysroot_postamble", d)
}


staging_packager () {

	mkdir -p ${PSTAGE_TMPDIR_STAGE}/CONTROL
	mkdir -p ${PSTAGE_DIR}/${PSTAGE_PKGPATH}

	echo "Package: ${PSTAGE_PKGPN}"         >  ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Version: ${PSTAGE_PKGVERSION}"    >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Description: ${DESCRIPTION}"      >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Section: ${SECTION}"              >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Priority: Optional"               >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Maintainer: ${MAINTAINER}"        >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	echo "Architecture: ${PSTAGE_PKGARCH}"  >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control
	
	# Protect against empty SRC_URI
	srcuri="${SRC_URI}"
	if [ "$srcuri" == "" ]; then		
		srcuri="OpenEmbedded"
	fi
	echo "Source: ${SRC_URI}"               >> ${PSTAGE_TMPDIR_STAGE}/CONTROL/control

	# Deal with libtool not supporting sysroots
	# Need to remove hardcoded paths and fix these when we install the
	# staging packages.
	# Could someone please add sysroot support to libtool!
        for i in `${PSTAGE_SCAN_CMD}` ; do \
                sed -i -e s:${STAGING_DIR}:FIXMESTAGINGDIR:g $i
                echo $i | sed -e 's:${PSTAGE_TMPDIR_STAGE}/::' >> ${PSTAGE_TMPDIR_STAGE}/sysroots/fixmepath
        done
        
	${PSTAGE_BUILD_CMD} ${PSTAGE_TMPDIR_STAGE} ${PSTAGE_DIR}/${PSTAGE_PKGPATH}
}

staging_package_installer () {
	#${PSTAGE_INSTALL_CMD} ${PSTAGE_PKG}

	STATUSFILE=${TMPDIR}${libdir_native}/opkg/status
	echo "Package: ${PSTAGE_PKGPN}"        >> $STATUSFILE
	echo "Version: ${PSTAGE_PKGVERSION}"   >> $STATUSFILE
	echo "Status: install user installed"  >> $STATUSFILE
	echo "Architecture: ${PSTAGE_PKGARCH}" >> $STATUSFILE
	echo "" >> $STATUSFILE

	CTRLFILE=${TMPDIR}${libdir_native}/opkg/info/${PSTAGE_PKGPN}.control
	echo "Package: ${PSTAGE_PKGPN}"        > $CTRLFILE
	echo "Version: ${PSTAGE_PKGVERSION}"   >> $CTRLFILE
	echo "Architecture: ${PSTAGE_PKGARCH}" >> $CTRLFILE

	cd ${PSTAGE_TMPDIR_STAGE}
	find -type f | grep -v ./CONTROL | sed -e 's/^\.//' > ${TMPDIR}${libdir_native}/opkg/info/${PSTAGE_PKGPN}.list
}

python staging_package_libtoolhack () {
	# Deal with libtool not supporting sysroots and add our new
	# staging location
        tmpdir = bb.data.getVar('TMPDIR', d, True)
        staging = bb.data.getVar('STAGING_DIR', d, True)
        fixmefn =  staging + "/fixmepath"
        try:
            fixmefd = open(fixmefn,"r")
            fixmefiles = fixmefd.readlines()
            fixmefd.close()
            os.system('rm -f ' + fixmefn)
            for file in fixmefiles:
                os.system("sed -i -e s:FIXMESTAGINGDIR:%s:g %s" % (staging, tmpdir + '/' + file))
        except IOError:
            pass
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
        if bb.data.inherits_class('package_rpm', d):
            rpmpath = bb.data.getVar('DEPLOY_DIR_RPM', d, True).replace(tmpdir, stagepath)

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

            if bb.data.inherits_class('package_rpm', d):
		version = bb.data.getVar('PV', d, 1)
		version = version.replace('-', '+')
		bb.data.setVar('RPMPV', version, d)
                srcname = bb.data.expand(pkgname + "-${RPMPV}-" + pr + ".${TARGET_ARCH}.rpm", d)
                srcfile = bb.data.expand("${DEPLOY_DIR_RPM}/" + arch + "/" + srcname, d)
                if os.path.exists(srcfile):
                    destpath = rpmpath + "/" + arch + "/" 
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

    pstage_set_pkgmanager(d)
    bb.build.exec_func("staging_helper", d)
    bb.build.exec_func("staging_packager", d)
    lf = bb.utils.lockfile(bb.data.expand("${SYSROOT_LOCK}", d))
    bb.build.exec_func("staging_package_installer", d)
    bb.utils.unlockfile(lf)
}

#
# Note an assumption here is that do_deploy runs before do_package_write/do_populate_sysroot
#
addtask package_stage after do_package_write do_populate_sysroot before do_build

do_package_stage_all () {
	:
}
do_package_stage_all[recrdeptask] = "do_package_stage"
addtask package_stage_all after do_package_stage before do_build
