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
	mkdir -p ${IMAGE_ROOTFS}/var/dpkg/info
	mkdir -p ${IMAGE_ROOTFS}/var/dpkg/updates

	rm -f ${STAGING_ETCDIR_NATIVE}/apt/sources.list.rev
	rm -f ${STAGING_ETCDIR_NATIVE}/apt/preferences
	> ${IMAGE_ROOTFS}/var/dpkg/status
	> ${IMAGE_ROOTFS}/var/dpkg/available
	mkdir -p ${IMAGE_ROOTFS}/var/dpkg/alternatives

	priority=1
	for arch in ${PACKAGE_ARCHS}; do
		if [ ! -d ${DEPLOY_DIR_DEB}/$arch ]; then
			continue;
		fi
		cd ${DEPLOY_DIR_DEB}/$arch
		# if [ -z "${DEPLOY_KEEP_PACKAGES}" ]; then
			rm -f Packages.gz Packages Packages.bz2
		# fi
		dpkg-scanpackages . | bzip2 > Packages.bz2
		echo "Label: $arch" > Release

		echo "deb file:${DEPLOY_DIR_DEB}/$arch/ ./" >> ${STAGING_ETCDIR_NATIVE}/apt/sources.list.rev
		(echo "Package: *"
		echo "Pin: release l=$arch"
		echo "Pin-Priority: $(expr 800 + $priority)"
		echo) >> ${STAGING_ETCDIR_NATIVE}/apt/preferences
		priority=$(expr $priority + 5)
	done

	tac ${STAGING_ETCDIR_NATIVE}/apt/sources.list.rev > ${STAGING_ETCDIR_NATIVE}/apt/sources.list

	cat "${STAGING_ETCDIR_NATIVE}/apt/apt.conf.sample" \
		| sed -e 's#Architecture ".*";#Architecture "${DPKG_ARCH}";#' \
		| sed -e 's:#ROOTFS#:${IMAGE_ROOTFS}:g' \
		> "${STAGING_ETCDIR_NATIVE}/apt/apt-rootfs.conf"

	export APT_CONFIG="${STAGING_ETCDIR_NATIVE}/apt/apt-rootfs.conf"
	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	apt-get update

	_flag () {
		sed -i -e "/^Package: $2\$/{n; s/Status: install ok .*/Status: install ok $1/;}" ${IMAGE_ROOTFS}/var/dpkg/status
	}
	_getflag () {
		cat ${IMAGE_ROOTFS}/var/dpkg/status | sed -n -e "/^Package: $2\$/{n; s/Status: install ok .*/$1/; p}"
	}

	if [ x${TARGET_OS} = "xlinux" ] || [ x${TARGET_OS} = "xlinux-gnueabi" ] ; then
		if [ ! -z "${LINGUAS_INSTALL}" ]; then
			apt-get install glibc-localedata-i18n --force-yes --allow-unauthenticated
			if [ $? -ne 0 ]; then
				exit 1
			fi
			for i in ${LINGUAS_INSTALL}; do
				apt-get install $i --force-yes --allow-unauthenticated
				if [ $? -ne 0 ]; then
					exit 1
				fi
			done
		fi
	fi

	if [ ! -z "${PACKAGE_INSTALL}" ]; then
		for i in ${PACKAGE_INSTALL}; do
			apt-get install $i --force-yes --allow-unauthenticated
			if [ $? -ne 0 ]; then
				exit 1
			fi
		done
	fi

	rm ${WORKDIR}/temp/log.do_$target-attemptonly.${PID}
	if [ ! -z "${PACKAGE_INSTALL_ATTEMPTONLY}" ]; then
		for i in ${PACKAGE_INSTALL_ATTEMPTONLY}; do
			apt-get install $i --force-yes --allow-unauthenticated >> ${WORKDIR}/temp/log.do_rootfs-attemptonly.${PID} || true
		done
	fi

	find ${IMAGE_ROOTFS} -name \*.dpkg-new | for i in `cat`; do
		mv $i `echo $i | sed -e's,\.dpkg-new$,,'`
	done

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	# Mark all packages installed
	sed -i -e "s/Status: install ok unpacked/Status: install ok installed/;" ${IMAGE_ROOTFS}/var/dpkg/status

	# Attempt to run preinsts
	# Mark packages with preinst failures as unpacked
	for i in ${IMAGE_ROOTFS}/var/dpkg/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
			_flag unpacked `basename $i .preinst`
		fi
	done

	# Attempt to run postinsts
	# Mark packages with postinst failures as unpacked
	for i in ${IMAGE_ROOTFS}/var/dpkg/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
			_flag unpacked `basename $i .postinst`
		fi
	done

	set -e

	# Hacks to allow opkg's update-alternatives and opkg to coexist for now
	mkdir -p ${IMAGE_ROOTFS}${opkglibdir}
	if [ -e ${IMAGE_ROOTFS}/var/dpkg/alternatives ]; then
		rmdir ${IMAGE_ROOTFS}/var/dpkg/alternatives
	fi
	ln -s ${opkglibdir}/alternatives ${IMAGE_ROOTFS}/var/dpkg/alternatives
	ln -s /var/dpkg/info ${IMAGE_ROOTFS}${opkglibdir}/info
	ln -s /var/dpkg/status ${IMAGE_ROOTFS}${opkglibdir}/status

	${ROOTFS_POSTPROCESS_COMMAND}

	log_check rootfs 
}

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
	rm -rf ${IMAGE_ROOTFS}/usr/dpkg/
}
