DEPENDS_prepend = "dpkg-native apt-native fakeroot-native "
DEPENDS_append = " ${EXTRA_IMAGEDEPENDS}"

fakeroot rootfs_deb_do_rootfs () {
	set +e
	mkdir -p ${IMAGE_ROOTFS}/var/dpkg/{info,updates}

	rm -f ${STAGING_DIR}/etc/apt/sources.list
	rm -f ${STAGING_DIR}/etc/apt/preferences
	> ${IMAGE_ROOTFS}/var/dpkg/status
	> ${IMAGE_ROOTFS}/var/dpkg/available
	# > ${STAGING_DIR}/var/dpkg/status

	priority=1
	for arch in ${PACKAGE_ARCHS}; do
		if [ ! -d ${DEPLOY_DIR_DEB}/$arch ]; then
			continue;
		fi
		cd ${DEPLOY_DIR_DEB}/$arch
		# if [ -z "${DEPLOY_KEEP_PACKAGES}" ]; then
			rm -f Packages.gz Packages Packages.bz2
		# fi
		apt-ftparchive packages . | bzip2 > Packages.bz2

		echo "deb file:${DEPLOY_DIR_DEB}/$arch/ ./" >> ${STAGING_DIR}/etc/apt/sources.list
		(echo "Package: *"
		echo "Pin origin ${DEPLOY_DIR_DEB}/$arch"
		echo "Pin-Priority: $((800 + $priority))") >> ${STAGING_DIR}/etc/apt/preferences
		priority=$(expr $priority + 5)
	done

	export APT_CONFIG="${STAGING_DIR}/etc/apt/apt.conf"
	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	apt-get update

	_flag () {
		sed -i -e "/^Package: $2\$/{n; s/Status: install ok .*/Status: install ok $1/;}" ${IMAGE_ROOTFS}/var/dpkg/status
	}
	_getflag () {
		cat ${IMAGE_ROOTFS}/var/dpkg/status | sed -n -e "/^Package: $2\$/{n; s/Status: install ok .*/$1/; p}"
	}

	if [ ! -z "${LINGUAS_INSTALL}" ]; then
		apt-get install glibc-localedata-i18n
		if [ $? -eq 1 ]; then
			exit 1
		fi
		for i in ${LINGUAS_INSTALL}; do
			apt-get install $i
			if [ $? -eq 1 ]; then
				exit 1
			fi
		done
	fi

	if [ ! -z "${PACKAGE_INSTALL}" ]; then
		for i in ${PACKAGE_INSTALL}; do
			apt-get install $i
			if [ $? -eq 1 ]; then
				exit 1
			fi
			find ${IMAGE_ROOTFS} -name \*.dpkg-new | for i in `cat`; do
				mv $i `echo $i | sed -e's,\.dpkg-new$,,'`
			done
		done
	fi

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	for i in ${IMAGE_ROOTFS}/var/dpkg/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
			_flag unpacked `basename $i .preinst`
		fi
	done

	for i in ${IMAGE_ROOTFS}/var/dpkg/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
			_flag unpacked `basename $i .postinst`
		fi
	done

	set -e

	${ROOTFS_POSTPROCESS_COMMAND}

	log_check rootfs 
}

rootfs_deb_log_check() {
	target="$1"
        lf_path="$2"

	lf_txt="`cat $lf_path`"
	for keyword_die in "E:"
	do				
		if (echo "$lf_txt" | grep -v log_check | grep "$keyword_die") &>/dev/null
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

