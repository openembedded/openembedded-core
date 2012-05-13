#
# Copyright 2006-2007 Openedhand Ltd.
#

ROOTFS_PKGMANAGE = "run-postinsts dpkg apt"
ROOTFS_PKGMANAGE_BOOTSTRAP  = "run-postinsts"

do_rootfs[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot"
do_rootfs[recrdeptask] += "do_package_write_deb"

DEB_POSTPROCESS_COMMANDS = "rootfs_install_all_locales; "

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

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

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

	log_check rootfs
}

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
	rm -rf ${IMAGE_ROOTFS}/usr/dpkg/
}

DPKG_QUERY_COMMAND = "${STAGING_BINDIR_NATIVE}/dpkg --admindir=${IMAGE_ROOTFS}/var/lib/dpkg"

list_installed_packages() {
	${DPKG_QUERY_COMMAND} -l | grep ^ii | awk '{ print $2 }'
}

get_package_filename() {
	fullname=`find ${DEPLOY_DIR_DEB} -name "$1_*.deb" || true`
	if [ "$fullname" = "" ] ; then
		echo $name
	else
		echo $fullname
	fi
}

list_package_depends() {
	${DPKG_QUERY_COMMAND} -s $1 | grep ^Depends | sed -e 's/^Depends: //' -e 's/,//g' -e 's:([=<>]* [^ )]*)::g'
}

list_package_recommends() {
	${DPKG_QUERY_COMMAND} -s $1 | grep ^Recommends | sed -e 's/^Recommends: //' -e 's/,//g' -e 's:([=<>]* [^ )]*)::g'
}

rootfs_check_package_exists() {
	if [ `apt-cache showpkg $1 | wc -l` -gt 2 ]; then
		echo $1
	fi
}

rootfs_install_packages() {
	${STAGING_BINDIR_NATIVE}/apt-get install $@ --force-yes --allow-unauthenticated

	for pkg in $@ ; do
		deb_package_setflag installed $pkg
	done
}
