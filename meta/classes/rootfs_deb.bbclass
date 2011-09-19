#
# Copyright 2006-2007 Openedhand Ltd.
#

ROOTFS_PKGMANAGE = "run-postinsts dpkg apt"
ROOTFS_PKGMANAGE_BOOTSTRAP  = "run-postinsts"

do_rootfs[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot"
do_rootfs[recrdeptask] += "do_package_write_deb"

opkglibdir = "${localstatedir}/lib/opkg"

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


	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	_flag () {
		sed -i -e "/^Package: $2\$/{n; s/Status: install ok .*/Status: install ok $1/;}" ${IMAGE_ROOTFS}/var/lib/dpkg/status
	}
	_getflag () {
		cat ${IMAGE_ROOTFS}/var/lib/dpkg/status | sed -n -e "/^Package: $2\$/{n; s/Status: install ok .*/$1/; p}"
	}

	# Attempt to run preinsts
	# Mark packages with preinst failures as unpacked
	for i in ${IMAGE_ROOTFS}/var/lib/dpkg/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
			_flag unpacked `basename $i .preinst`
		fi
	done

	# Attempt to run postinsts
	# Mark packages with postinst failures as unpacked
	for i in ${IMAGE_ROOTFS}/var/lib/dpkg/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
			_flag unpacked `basename $i .postinst`
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
