#
# Copyright 2006-2008 OpenedHand Ltd.
#

inherit package

IMAGE_PKGTYPE ?= "deb"

DPKG_ARCH ?= "${TARGET_ARCH}" 

PKGWRITEDIRDEB = "${WORKDIR}/deploy-debs"

python package_deb_fn () {
    bb.data.setVar('PKGFN', bb.data.getVar('PKG',d), d)
}

addtask package_deb_install
python do_package_deb_install () {
    pkg = bb.data.getVar('PKG', d, True)
    pkgfn = bb.data.getVar('PKGFN', d, True)
    rootfs = bb.data.getVar('IMAGE_ROOTFS', d, True)
    debdir = bb.data.getVar('DEPLOY_DIR_DEB', d, True)
    apt_config = bb.data.expand('${STAGING_ETCDIR_NATIVE}/apt/apt.conf', d)
    stagingbindir = bb.data.getVar('STAGING_BINDIR_NATIVE', d, True)
    tmpdir = bb.data.getVar('TMPDIR', d, True)

    if None in (pkg,pkgfn,rootfs):
        raise bb.build.FuncFailed("missing variables (one or more of PKG, PKGFN, IMAGE_ROOTFS)")
    try:
        if not os.exists(rootfs):
            os.makedirs(rootfs)
        os.chdir(rootfs)
    except OSError:
        import sys
        raise bb.build.FuncFailed(str(sys.exc_value))

    # update packages file
    (exitstatus, output) = commands.getstatusoutput('dpkg-scanpackages %s > %s/Packages' % (debdir, debdir))
    if (exitstatus != 0 ):
        raise bb.build.FuncFailed(output)

    f = open(os.path.join(tmpdir, "stamps", "DEB_PACKAGE_INDEX_CLEAN"), "w")
    f.close()

    # NOTE: this env stuff is racy at best, we need something more capable
    # than 'commands' for command execution, which includes manipulating the
    # env of the fork+execve'd processs

    # Set up environment
    apt_config_backup = os.getenv('APT_CONFIG')
    os.putenv('APT_CONFIG', apt_config)
    path = os.getenv('PATH')
    os.putenv('PATH', '%s:%s' % (stagingbindir, os.getenv('PATH')))

    # install package
    commands.getstatusoutput('apt-get update')
    commands.getstatusoutput('apt-get install -y %s' % pkgfn)

    # revert environment
    os.putenv('APT_CONFIG', apt_config_backup)
    os.putenv('PATH', path)
}

#
# Update the Packages index files in ${DEPLOY_DIR_DEB}
#
package_update_index_deb () {

	local debarchs=""

	if [ ! -z "${DEPLOY_KEEP_PACKAGES}" ]; then
		return
	fi

	for arch in ${PACKAGE_ARCHS} ${SDK_PACKAGE_ARCHS}; do
		if [ -e ${DEPLOY_DIR_DEB}/$arch ]; then
			debarchs="$debarchs $arch"
		fi
	done

	for arch in $debarchs; do
		if [ ! -d ${DEPLOY_DIR_DEB}/$arch ]; then
			continue;
		fi
		cd ${DEPLOY_DIR_DEB}/$arch
		dpkg-scanpackages . | bzip2 > Packages.bz2
		echo "Label: $arch" > Release
	done
}

#
# install a bunch of packages using apt
# the following shell variables needs to be set before calling this func:
# INSTALL_ROOTFS_DEB - install root dir
# INSTALL_BASEARCH_DEB - install base architecutre
# INSTALL_ARCHS_DEB - list of available archs
# INSTALL_PACKAGES_NORMAL_DEB - packages to be installed
# INSTALL_PACKAGES_ATTEMPTONLY_DEB - packages attemped to be installed only
# INSTALL_PACKAGES_LINGUAS_DEB - additional packages for uclibc
# INSTALL_TASK_DEB - task name

package_install_internal_deb () {

	local target_rootfs="${INSTALL_ROOTFS_DEB}"
	local dpkg_arch="${INSTALL_BASEARCH_DEB}"
	local archs="${INSTALL_ARCHS_DEB}"
	local package_to_install="${INSTALL_PACKAGES_NORMAL_DEB}"
	local package_attemptonly="${INSTALL_PACKAGES_ATTEMPTONLY_DEB}"
	local package_linguas="${INSTALL_PACKAGES_LINGUAS_DEB}"
	local task="${INSTALL_TASK_DEB}"

	rm -f ${STAGING_ETCDIR_NATIVE}/apt/sources.list.rev
	rm -f ${STAGING_ETCDIR_NATIVE}/apt/preferences

	priority=1
	for arch in $archs; do
		if [ ! -d ${DEPLOY_DIR_DEB}/$arch ]; then
			continue;
		fi

		echo "deb file:${DEPLOY_DIR_DEB}/$arch/ ./" >> ${STAGING_ETCDIR_NATIVE}/apt/sources.list.rev
		(echo "Package: *"
		echo "Pin: release l=$arch"
		echo "Pin-Priority: $(expr 800 + $priority)"
		echo) >> ${STAGING_ETCDIR_NATIVE}/apt/preferences
		priority=$(expr $priority + 5)
	done

	tac ${STAGING_ETCDIR_NATIVE}/apt/sources.list.rev > ${STAGING_ETCDIR_NATIVE}/apt/sources.list

	cat "${STAGING_ETCDIR_NATIVE}/apt/apt.conf.sample" \
		| sed -e "s#Architecture \".*\";#Architecture \"${dpkg_arch}\";#" \
		| sed -e "s:#ROOTFS#:${target_rootfs}:g" \
		> "${STAGING_ETCDIR_NATIVE}/apt/apt-${task}.conf"

	export APT_CONFIG="${STAGING_ETCDIR_NATIVE}/apt/apt-${task}.conf"

	mkdir -p ${target_rootfs}/var/dpkg/info
	mkdir -p ${target_rootfs}/var/dpkg/updates

	> ${target_rootfs}/var/dpkg/status
	> ${target_rootfs}/var/dpkg/available

	apt-get update

	# Uclibc builds don't provide this stuff..
	if [ x${TARGET_OS} = "xlinux" ] || [ x${TARGET_OS} = "xlinux-gnueabi" ] ; then
		if [ ! -z "${package_linguas}" ]; then
			apt-get install glibc-localedata-i18n --force-yes --allow-unauthenticated
			if [ $? -ne 0 ]; then
				exit 1
			fi
			for i in ${package_linguas}; do
				apt-get install $i --force-yes --allow-unauthenticated
				if [ $? -ne 0 ]; then
					exit 1
				fi
			done
		fi
	fi

	# normal install
	for i in ${package_to_install}; do
		apt-get install $i --force-yes --allow-unauthenticated
		if [ $? -ne 0 ]; then
			exit 1
		fi
	done

	rm -f ${WORKDIR}/temp/log.do_${task}-attemptonly.${PID}
	if [ ! -z "${package_attemptonly}" ]; then
		for i in ${package_attemptonly}; do
			apt-get install $i --force-yes --allow-unauthenticated >> ${WORKDIR}/temp/log.do_${task}-attemptonly.${PID} 2>&1 || true
		done
	fi

	find ${target_rootfs} -name \*.dpkg-new | for i in `cat`; do
		mv $i `echo $i | sed -e's,\.dpkg-new$,,'`
	done

	# Mark all packages installed
	sed -i -e "s/Status: install ok unpacked/Status: install ok installed/;" ${target_rootfs}/var/dpkg/status
}

deb_log_check() {
	target="$1"
	lf_path="$2"

	lf_txt="`cat $lf_path`"
	for keyword_die in "E:"
	do
		if (echo "$lf_txt" | grep -v log_check | grep "$keyword_die") >/dev/null 2>&1
		then
			echo "log_check: There were error messages in the logfile"
			echo -e "log_check: Matched keyword: [$keyword_die]\n"
			echo "$lf_txt" | grep -v log_check | grep -C 5 -i "$keyword_die"
			echo ""
			do_exit=1
		fi
	done
	test "$do_exit" = 1 && exit 1
	true
}

python do_package_deb () {
    import re, copy
    import textwrap

    workdir = bb.data.getVar('WORKDIR', d, True)
    if not workdir:
        bb.error("WORKDIR not defined, unable to package")
        return

    outdir = bb.data.getVar('PKGWRITEDIRDEB', d, True)
    if not outdir:
        bb.error("PKGWRITEDIRDEB not defined, unable to package")
        return

    packages = bb.data.getVar('PACKAGES', d, True)
    if not packages:
        bb.debug(1, "PACKAGES not defined, nothing to package")
        return

    tmpdir = bb.data.getVar('TMPDIR', d, True)

    if os.access(os.path.join(tmpdir, "stamps", "DEB_PACKAGE_INDEX_CLEAN"),os.R_OK):
        os.unlink(os.path.join(tmpdir, "stamps", "DEB_PACKAGE_INDEX_CLEAN"))

    if packages == []:
        bb.debug(1, "No packages; nothing to do")
        return

    pkgdest = bb.data.getVar('PKGDEST', d, True)

    for pkg in packages.split():
        localdata = bb.data.createCopy(d)
        root = "%s/%s" % (pkgdest, pkg)

        lf = bb.utils.lockfile(root + ".lock")

        bb.data.setVar('ROOT', '', localdata)
        bb.data.setVar('ROOT_%s' % pkg, root, localdata)
        pkgname = bb.data.getVar('PKG_%s' % pkg, localdata, True)
        if not pkgname:
            pkgname = pkg
        bb.data.setVar('PKG', pkgname, localdata)

        bb.data.setVar('OVERRIDES', pkg, localdata)

        bb.data.update_data(localdata)
        basedir = os.path.join(os.path.dirname(root))

        pkgoutdir = os.path.join(outdir, bb.data.getVar('PACKAGE_ARCH', localdata, True))
        bb.mkdirhier(pkgoutdir)

        os.chdir(root)
        from glob import glob
        g = glob('*')
        try:
            del g[g.index('DEBIAN')]
            del g[g.index('./DEBIAN')]
        except ValueError:
            pass
        if not g and bb.data.getVar('ALLOW_EMPTY', localdata) != "1":
            bb.note("Not creating empty archive for %s-%s-%s" % (pkg, bb.data.getVar('PKGV', localdata, True), bb.data.getVar('PKGR', localdata, True)))
            bb.utils.unlockfile(lf)
            continue

        controldir = os.path.join(root, 'DEBIAN')
        bb.mkdirhier(controldir)
        os.chmod(controldir, 0755)
        try:
            ctrlfile = file(os.path.join(controldir, 'control'), 'wb')
            # import codecs
            # ctrlfile = codecs.open("someFile", "w", "utf-8")
        except OSError:
            bb.utils.unlockfile(lf)
            raise bb.build.FuncFailed("unable to open control file for writing.")

        fields = []
        pe = bb.data.getVar('PKGE', d, True)
        if pe and int(pe) > 0:
            fields.append(["Version: %s:%s-%s\n", ['PKGE', 'PKGV', 'PKGR']])
        else:
            fields.append(["Version: %s-%s\n", ['PKGV', 'PKGR']])
        fields.append(["Description: %s\n", ['DESCRIPTION']])
        fields.append(["Section: %s\n", ['SECTION']])
        fields.append(["Priority: %s\n", ['PRIORITY']])
        fields.append(["Maintainer: %s\n", ['MAINTAINER']])
        fields.append(["Architecture: %s\n", ['DPKG_ARCH']])
        fields.append(["OE: %s\n", ['PN']])
        fields.append(["Homepage: %s\n", ['HOMEPAGE']])

        # Package, Version, Maintainer, Description - mandatory
        # Section, Priority, Essential, Architecture, Source, Depends, Pre-Depends, Recommends, Suggests, Conflicts, Replaces, Provides - Optional


        def pullData(l, d):
            l2 = []
            for i in l:
                data = bb.data.getVar(i, d, True)
                if data is None:
                    raise KeyError(f)
                if i == 'DPKG_ARCH' and bb.data.getVar('PACKAGE_ARCH', d, True) == 'all':
                    data = 'all'
                l2.append(data)
            return l2

        ctrlfile.write("Package: %s\n" % pkgname)
        # check for required fields
        try:
            for (c, fs) in fields:
                for f in fs:
                     if bb.data.getVar(f, localdata) is None:
                         raise KeyError(f)
                # Special behavior for description...
                if 'DESCRIPTION' in fs:
                     summary = bb.data.getVar('SUMMARY', localdata, True) or bb.data.getVar('DESCRIPTION', localdata, True) or "."
                     description = bb.data.getVar('DESCRIPTION', localdata, True) or "."
                     description = textwrap.dedent(description).strip()
                     ctrlfile.write('Description: %s\n' % unicode(summary))
                     ctrlfile.write('%s\n' % unicode(textwrap.fill(description, width=74, initial_indent=' ', subsequent_indent=' ')))
                else:
                     ctrlfile.write(unicode(c % tuple(pullData(fs, localdata))))
        except KeyError:
            import sys
            (type, value, traceback) = sys.exc_info()
            bb.utils.unlockfile(lf)
            ctrlfile.close()
            raise bb.build.FuncFailed("Missing field for deb generation: %s" % value)
        # more fields

        bb.build.exec_func("mapping_rename_hook", localdata)

        rdepends = bb.utils.explode_dep_versions(bb.data.getVar("RDEPENDS", localdata, True) or "")
        for dep in rdepends:
                if '*' in dep:
                        del rdepends[dep]
        rrecommends = bb.utils.explode_dep_versions(bb.data.getVar("RRECOMMENDS", localdata, True) or "")
        for dep in rrecommends:
                if '*' in dep:
                        del rrecommends[dep]
        rsuggests = bb.utils.explode_dep_versions(bb.data.getVar("RSUGGESTS", localdata, True) or "")
        rprovides = bb.utils.explode_dep_versions(bb.data.getVar("RPROVIDES", localdata, True) or "")
        rreplaces = bb.utils.explode_dep_versions(bb.data.getVar("RREPLACES", localdata, True) or "")
        rconflicts = bb.utils.explode_dep_versions(bb.data.getVar("RCONFLICTS", localdata, True) or "")
        if rdepends:
            ctrlfile.write("Depends: %s\n" % unicode(bb.utils.join_deps(rdepends)))
        if rsuggests:
            ctrlfile.write("Suggests: %s\n" % unicode(bb.utils.join_deps(rsuggests)))
        if rrecommends:
            ctrlfile.write("Recommends: %s\n" % unicode(bb.utils.join_deps(rrecommends)))
        if rprovides:
            ctrlfile.write("Provides: %s\n" % unicode(bb.utils.join_deps(rprovides)))
        if rreplaces:
            ctrlfile.write("Replaces: %s\n" % unicode(bb.utils.join_deps(rreplaces)))
        if rconflicts:
            ctrlfile.write("Conflicts: %s\n" % unicode(bb.utils.join_deps(rconflicts)))
        ctrlfile.close()

        for script in ["preinst", "postinst", "prerm", "postrm"]:
            scriptvar = bb.data.getVar('pkg_%s' % script, localdata, True)
            if not scriptvar:
                continue
            try:
                scriptfile = file(os.path.join(controldir, script), 'w')
            except OSError:
                bb.utils.unlockfile(lf)
                raise bb.build.FuncFailed("unable to open %s script file for writing." % script)
            scriptfile.write("#!/bin/sh\n")
            scriptfile.write(scriptvar)
            scriptfile.close()
            os.chmod(os.path.join(controldir, script), 0755)

        conffiles_str = bb.data.getVar("CONFFILES", localdata, True)
        if conffiles_str:
            try:
                conffiles = file(os.path.join(controldir, 'conffiles'), 'w')
            except OSError:
                bb.utils.unlockfile(lf)
                raise bb.build.FuncFailed("unable to open conffiles for writing.")
            for f in conffiles_str.split():
                conffiles.write('%s\n' % f)
            conffiles.close()

        os.chdir(basedir)
        ret = os.system("PATH=\"%s\" dpkg-deb -b %s %s" % (bb.data.getVar("PATH", localdata, True), root, pkgoutdir))
        if ret != 0:
            bb.utils.prunedir(controldir)
            bb.utils.unlockfile(lf)
            raise bb.build.FuncFailed("dpkg-deb execution failed")

        bb.utils.prunedir(controldir)
        bb.utils.unlockfile(lf)
}

SSTATETASKS += "do_package_write_deb"
do_package_write_deb[sstate-name] = "deploy-deb"
do_package_write_deb[sstate-inputdirs] = "${PKGWRITEDIRDEB}"
do_package_write_deb[sstate-outputdirs] = "${DEPLOY_DIR_DEB}"

python do_package_write_deb_setscene () {
    sstate_setscene(d)
}
addtask do_package_write_deb_setscene

python () {
    if bb.data.getVar('PACKAGES', d, True) != '':
        deps = (bb.data.getVarFlag('do_package_write_deb', 'depends', d) or "").split()
        deps.append('dpkg-native:do_populate_sysroot')
        deps.append('virtual/fakeroot-native:do_populate_sysroot')
        bb.data.setVarFlag('do_package_write_deb', 'depends', " ".join(deps), d)
        bb.data.setVarFlag('do_package_write_deb', 'fakeroot', "1", d)
        bb.data.setVarFlag('do_package_write_deb_setscene', 'fakeroot', "1", d)

    # Map TARGET_ARCH to Debian's ideas about architectures
    if bb.data.getVar('DPKG_ARCH', d, True) in ["x86", "i486", "i586", "i686", "pentium"]:
        bb.data.setVar('DPKG_ARCH', 'i386', d)
}

python do_package_write_deb () {
	bb.build.exec_func("read_subpackage_metadata", d)
	bb.build.exec_func("do_package_deb", d)
}
do_package_write_deb[dirs] = "${PKGWRITEDIRDEB}"
addtask package_write_deb before do_package_write after do_package


PACKAGEINDEXES += "package_update_index_deb;"
PACKAGEINDEXDEPS += "dpkg-native:do_populate_sysroot"
PACKAGEINDEXDEPS += "apt-native:do_populate_sysroot"
