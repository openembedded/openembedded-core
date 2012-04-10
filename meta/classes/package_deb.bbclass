#
# Copyright 2006-2008 OpenedHand Ltd.
#

inherit package

IMAGE_PKGTYPE ?= "deb"

DPKG_ARCH ?= "${TARGET_ARCH}" 

PKGWRITEDIRDEB = "${WORKDIR}/deploy-debs"

python package_deb_fn () {
    d.setVar('PKGFN', d.getVar('PKG'))
}

addtask package_deb_install
python do_package_deb_install () {
    pkg = d.getVar('PKG', True)
    pkgfn = d.getVar('PKGFN', True)
    rootfs = d.getVar('IMAGE_ROOTFS', True)
    debdir = d.getVar('DEPLOY_DIR_DEB', True)
    apt_config = d.expand('${STAGING_ETCDIR_NATIVE}/apt/apt.conf')
    stagingbindir = d.getVar('STAGING_BINDIR_NATIVE', True)
    tmpdir = d.getVar('TMPDIR', True)

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
		dpkg-scanpackages . | gzip > Packages.gz
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

	mkdir -p ${target_rootfs}/var/lib/dpkg/info
	mkdir -p ${target_rootfs}/var/lib/dpkg/updates

	> ${target_rootfs}/var/lib/dpkg/status
	> ${target_rootfs}/var/lib/dpkg/available

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
	sed -i -e "s/Status: install ok unpacked/Status: install ok installed/;" ${target_rootfs}/var/lib/dpkg/status
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

    workdir = d.getVar('WORKDIR', True)
    if not workdir:
        bb.error("WORKDIR not defined, unable to package")
        return

    outdir = d.getVar('PKGWRITEDIRDEB', True)
    if not outdir:
        bb.error("PKGWRITEDIRDEB not defined, unable to package")
        return

    packages = d.getVar('PACKAGES', True)
    if not packages:
        bb.debug(1, "PACKAGES not defined, nothing to package")
        return

    tmpdir = d.getVar('TMPDIR', True)

    if os.access(os.path.join(tmpdir, "stamps", "DEB_PACKAGE_INDEX_CLEAN"),os.R_OK):
        os.unlink(os.path.join(tmpdir, "stamps", "DEB_PACKAGE_INDEX_CLEAN"))

    if packages == []:
        bb.debug(1, "No packages; nothing to do")
        return

    pkgdest = d.getVar('PKGDEST', True)

    for pkg in packages.split():
        localdata = bb.data.createCopy(d)
        root = "%s/%s" % (pkgdest, pkg)

        lf = bb.utils.lockfile(root + ".lock")

        localdata.setVar('ROOT', '')
        localdata.setVar('ROOT_%s' % pkg, root)
        pkgname = localdata.getVar('PKG_%s' % pkg, True)
        if not pkgname:
            pkgname = pkg
        localdata.setVar('PKG', pkgname)

        localdata.setVar('OVERRIDES', pkg)

        bb.data.update_data(localdata)
        basedir = os.path.join(os.path.dirname(root))

        pkgoutdir = os.path.join(outdir, localdata.getVar('PACKAGE_ARCH', True))
        bb.mkdirhier(pkgoutdir)

        os.chdir(root)
        from glob import glob
        g = glob('*')
        try:
            del g[g.index('DEBIAN')]
            del g[g.index('./DEBIAN')]
        except ValueError:
            pass
        if not g and localdata.getVar('ALLOW_EMPTY') != "1":
            bb.note("Not creating empty archive for %s-%s-%s" % (pkg, localdata.getVar('PKGV', True), localdata.getVar('PKGR', True)))
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
        pe = d.getVar('PKGE', True)
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
                data = d.getVar(i, True)
                if data is None:
                    raise KeyError(f)
                if i == 'DPKG_ARCH' and d.getVar('PACKAGE_ARCH', True) == 'all':
                    data = 'all'
                l2.append(data)
            return l2

        ctrlfile.write("Package: %s\n" % pkgname)
        # check for required fields
        try:
            for (c, fs) in fields:
                for f in fs:
                     if localdata.getVar(f) is None:
                         raise KeyError(f)
                # Special behavior for description...
                if 'DESCRIPTION' in fs:
                     summary = localdata.getVar('SUMMARY', True) or localdata.getVar('DESCRIPTION', True) or "."
                     description = localdata.getVar('DESCRIPTION', True) or "."
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

        rdepends = bb.utils.explode_dep_versions(localdata.getVar("RDEPENDS", True) or "")
        for dep in rdepends:
                if '*' in dep:
                        del rdepends[dep]
        rrecommends = bb.utils.explode_dep_versions(localdata.getVar("RRECOMMENDS", True) or "")
        for dep in rrecommends:
                if '*' in dep:
                        del rrecommends[dep]
        rsuggests = bb.utils.explode_dep_versions(localdata.getVar("RSUGGESTS", True) or "")
        rprovides = bb.utils.explode_dep_versions(localdata.getVar("RPROVIDES", True) or "")
        rreplaces = bb.utils.explode_dep_versions(localdata.getVar("RREPLACES", True) or "")
        rconflicts = bb.utils.explode_dep_versions(localdata.getVar("RCONFLICTS", True) or "")
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
            scriptvar = localdata.getVar('pkg_%s' % script, True)
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

        conffiles_str = localdata.getVar("CONFFILES", True)
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
        ret = os.system("PATH=\"%s\" dpkg-deb -b %s %s" % (localdata.getVar("PATH", True), root, pkgoutdir))
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
    if d.getVar('PACKAGES', True) != '':
        deps = ' dpkg-native:do_populate_sysroot virtual/fakeroot-native:do_populate_sysroot'
        d.appendVarFlag('do_package_write_deb', 'depends', deps)
        d.setVarFlag('do_package_write_deb', 'fakeroot', "1")
        d.setVarFlag('do_package_write_deb_setscene', 'fakeroot', "1")

    # Map TARGET_ARCH to Debian's ideas about architectures
    if d.getVar('DPKG_ARCH', True) in ["x86", "i486", "i586", "i686", "pentium"]:
        d.setVar('DPKG_ARCH', 'i386')
}

python do_package_write_deb () {
	bb.build.exec_func("read_subpackage_metadata", d)
	bb.build.exec_func("do_package_deb", d)
}
do_package_write_deb[dirs] = "${PKGWRITEDIRDEB}"
do_package_write_deb[umask] = "022"
addtask package_write_deb before do_package_write after do_package


PACKAGEINDEXES += "package_update_index_deb;"
PACKAGEINDEXDEPS += "dpkg-native:do_populate_sysroot"
PACKAGEINDEXDEPS += "apt-native:do_populate_sysroot"
