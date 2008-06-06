#
# Creates a root filesystem out of IPKs
#
# This rootfs can be mounted via root-nfs or it can be put into an cramfs/jffs etc.
# See image.bbclass for a usage of this.
#

ROOTFS_PKGMANAGE = "opkg opkg-collateral"
ROOTFS_PKGMANAGE_BOOTSTRAP  = "run-postinsts"

do_rootfs[depends] += "opkg-native:do_populate_staging ipkg-utils-native:do_populate_staging"
do_rootfs[recrdeptask] += "do_package_write_ipk"

IPKG_ARGS = "-f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS}"

fakeroot rootfs_ipk_do_rootfs () {
	set -x

	package_update_index_ipk
	package_generate_ipkg_conf

	mkdir -p ${T}/
	mkdir -p ${IMAGE_ROOTFS}/usr/lib/opkg/

	opkg-cl ${IPKG_ARGS} update

	# Uclibc builds don't provide this stuff...
	if [ x${TARGET_OS} = "xlinux" ] || [ x${TARGET_OS} = "xlinux-gnueabi" ] ; then 
		if [ ! -z "${LINGUAS_INSTALL}" ]; then
			for i in ${LINGUAS_INSTALL}; do
				opkg-cl ${IPKG_ARGS} install $i 
			done
		fi
	fi
	if [ ! -z "${PACKAGE_INSTALL}" ]; then
		opkg-cl ${IPKG_ARGS} install ${PACKAGE_INSTALL}
	fi

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IPKG_OFFLINE_ROOT}

	mkdir -p ${IMAGE_ROOTFS}/etc/opkg/
	grep "^arch" ${IPKGCONF_TARGET} >${IMAGE_ROOTFS}/etc/opkg/arch.conf

	${ROOTFS_POSTINSTALL_COMMAND}
	
	for i in ${IMAGE_ROOTFS}${libdir}/opkg/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
			opkg-cl ${IPKG_ARGS} flag unpacked `basename $i .preinst`
		fi
	done
	for i in ${IMAGE_ROOTFS}${libdir}/opkg/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
			opkg-cl ${IPKG_ARGS} flag unpacked `basename $i .postinst`
		fi
	done

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	${ROOTFS_POSTPROCESS_COMMAND}
	
	rm -f ${IMAGE_ROOTFS}${libdir}/opkg/lists/*

	log_check rootfs 	
}

rootfs_ipk_log_check() {
	target="$1"
        lf_path="$2"

	lf_txt="`cat $lf_path`"
	for keyword_die in "Cannot find package" "exit 1" ERR Fail
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

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}/usr/lib/opkg/
}

install_all_locales() {

    PACKAGES_TO_INSTALL=""

    for pkg in `grep ^Package: ${IMAGE_ROOTFS}${libdir}/opkg/status |sed "s/^Package: //"|egrep -v -- "(-locale-|-dev$|-doc$|^kernel|^glibc|^ttf|^task|^perl|^python)"`
    do
        for lang in ${IMAGE_LOCALES}
        do
            if [ `opkg-cl ${IPKG_ARGS} info $pkg-locale-$lang | wc -l` -gt 0 ]
            then
                    PACKAGES_TO_INSTALL="$PACKAGES_TO_INSTALL $pkg-locale-$lang"
            fi
        done
    done
    if [ "$PACKAGES_TO_INSTALL" != "" ]
    then
        opkg-cl ${IPKG_ARGS} install $PACKAGES_TO_INSTALL
    fi
}

