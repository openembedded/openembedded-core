#
# Copyright 2006-2007 Openedhand Ltd.
#

ROOTFS_PKGMANAGE = "dpkg apt"
ROOTFS_PKGMANAGE_BOOTSTRAP  = "run-postinsts"

do_rootfs[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot"
do_rootfs[recrdeptask] += "do_package_write_deb"
rootfs_deb_do_rootfs[vardepsexclude] += "BUILDNAME"

do_rootfs[lockfiles] += "${DEPLOY_DIR_DEB}/deb.lock"

python rootfs_deb_bad_recommendations() {
    if d.getVar("BAD_RECOMMENDATIONS", True):
	bb.warn("Debian package install does not support BAD_RECOMMENDATIONS")
}
do_rootfs[prefuncs] += "rootfs_deb_bad_recommendations"

DEB_POSTPROCESS_COMMANDS = ""

opkglibdir = "${localstatedir}/lib/opkg"

deb_package_setflag() {
	sed -i -e "/^Package: $2\$/{n; s/Status: install ok .*/Status: install ok $1/;}" ${IMAGE_ROOTFS}/var/lib/dpkg/status
}

deb_package_getflag() {
	cat ${IMAGE_ROOTFS}/var/lib/dpkg/status | sed -n -e "/^Package: $2\$/{n; s/Status: install ok .*/$1/; p}"
}

fakeroot rootfs_deb_do_rootfs () {
	set +e

	mkdir -p ${IMAGE_ROOTFS}/var/lib/dpkg/alternatives

	# update index
	package_update_index_deb

	#install packages
	export INSTALL_ROOTFS_DEB="${IMAGE_ROOTFS}"
	export INSTALL_BASEARCH_DEB="${DPKG_ARCH}"
	export INSTALL_ARCHS_DEB="${PACKAGE_ARCHS}"
	export INSTALL_PACKAGES_NORMAL_DEB="${PACKAGE_INSTALL}"
	export INSTALL_PACKAGES_ATTEMPTONLY_DEB="${PACKAGE_INSTALL_ATTEMPTONLY}"
	export INSTALL_PACKAGES_LINGUAS_DEB="${LINGUAS_INSTALL}"
	export INSTALL_TASK_DEB="rootfs"

	package_install_internal_deb
	${DEB_POSTPROCESS_COMMANDS}

	rootfs_install_complementary

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export INTERCEPT_DIR=${WORKDIR}/intercept_scripts
	export NATIVE_ROOT=${STAGING_DIR_NATIVE}

	# Attempt to run preinsts
	# Mark packages with preinst failures as unpacked
	for i in ${IMAGE_ROOTFS}/var/lib/dpkg/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
			deb_package_setflag unpacked `basename $i .preinst`
		fi
	done

	# Attempt to run postinsts
	# Mark packages with postinst failures as unpacked
	for i in ${IMAGE_ROOTFS}/var/lib/dpkg/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
			deb_package_setflag unpacked `basename $i .postinst`
		fi
	done

	set -e

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	# Hacks to allow opkg's update-alternatives and opkg to coexist for now
	mkdir -p ${IMAGE_ROOTFS}${opkglibdir}
	if [ -e ${IMAGE_ROOTFS}/var/lib/dpkg/alternatives ]; then
		rmdir ${IMAGE_ROOTFS}/var/lib/dpkg/alternatives
	fi
	ln -s ${opkglibdir}/alternatives ${IMAGE_ROOTFS}/var/lib/dpkg/alternatives
	ln -s /var/lib/dpkg/info ${IMAGE_ROOTFS}${opkglibdir}/info
	ln -s /var/lib/dpkg/status ${IMAGE_ROOTFS}${opkglibdir}/status

	${ROOTFS_POSTPROCESS_COMMAND}

	if ${@base_contains("IMAGE_FEATURES", "read-only-rootfs", "true", "false" ,d)}; then
	        delayed_postinsts="$(delayed_postinsts)"
		if [ -n "$delayed_postinsts" ]; then
			bberror "The following packages could not be configured offline and rootfs is read-only: $delayed_postinsts"
			exit 1
		fi
	fi

	log_check rootfs
}

rootfs_deb_do_rootfs[vardeps] += "delayed_postinsts"

delayed_postinsts () {
	cat ${IMAGE_ROOTFS}/var/lib/dpkg/status|grep -e "^Package:" -e "^Status:"|sed -ne 'N;s/Package: \(.*\)\nStatus:.*unpacked/\1/p'
}

save_postinsts () {
	# Scripts need to be ordered when executed, this ensures numeric order
	# If we ever run into needing more the 899 scripts, we'll have to
	# change num to start with 1000.
	num=100
	for p in $(delayed_postinsts); do
		install -d ${IMAGE_ROOTFS}${sysconfdir}/deb-postinsts
		cp ${IMAGE_ROOTFS}/var/lib/dpkg/info/$p.postinst ${IMAGE_ROOTFS}${sysconfdir}/deb-postinsts/$num-$p
		num=`echo \$((num+1))`
	done
}

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
	rm -rf ${IMAGE_ROOTFS}/var/lib/dpkg/
}

rootfs_install_packages() {
	${STAGING_BINDIR_NATIVE}/apt-get ${APT_ARGS} install `cat $1` --force-yes --allow-unauthenticated

	# Mark all packages installed
	sed -i -e "s/Status: install ok unpacked/Status: install ok installed/;" $INSTALL_ROOTFS_DEB/var/lib/dpkg/status
}

rootfs_uninstall_packages() {
	# for some reason, --root doesn't really work here... We use --admindir&--instdir instead.
	${STAGING_BINDIR_NATIVE}/dpkg --admindir=${IMAGE_ROOTFS}/var/lib/dpkg --instdir=${IMAGE_ROOTFS} -r --force-depends $@
}

