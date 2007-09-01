#
# Creates a root filesystem out of IPKs
#
# This rootfs can be mounted via root-nfs or it can be put into an cramfs/jffs etc.
# See image.bbclass for a usage of this.
#

do_rootfs[depends] += "ipkg-native:do_populate_staging ipkg-utils-native:do_populate_staging"
do_rootfs[recrdeptask] += "do_package_write_ipk"

IPKG_ARGS = "-f ${T}/ipkg.conf -o ${IMAGE_ROOTFS}"

rootfs_ipk_do_indexes () {
	set -x

	ipkgarchs="${PACKAGE_ARCHS}"

	if [ -z "${DEPLOY_KEEP_PACKAGES}" ]; then
		touch ${DEPLOY_DIR_IPK}/Packages
		ipkg-make-index -r ${DEPLOY_DIR_IPK}/Packages -p ${DEPLOY_DIR_IPK}/Packages -l ${DEPLOY_DIR_IPK}/Packages.filelist -m ${DEPLOY_DIR_IPK}
	fi

	for arch in $ipkgarchs; do
		if [ -z "${DEPLOY_KEEP_PACKAGES}" ]; then
			if [ -e ${DEPLOY_DIR_IPK}/$arch/ ] ; then 
				touch ${DEPLOY_DIR_IPK}/$arch/Packages
				ipkg-make-index -r ${DEPLOY_DIR_IPK}/$arch/Packages -p ${DEPLOY_DIR_IPK}/$arch/Packages -l ${DEPLOY_DIR_IPK}/$arch/Packages.filelist -m ${DEPLOY_DIR_IPK}/$arch/
			fi
		fi
	done
}

fakeroot rootfs_ipk_do_rootfs () {
	set -x

	rootfs_ipk_do_indexes

	mkdir -p ${T}
	echo "src oe file:${DEPLOY_DIR_IPK}" > ${T}/ipkg.conf
	ipkgarchs="${PACKAGE_ARCHS}"
	priority=1
	for arch in $ipkgarchs; do
		echo "arch $arch $priority" >> ${T}/ipkg.conf
		priority=$(expr $priority + 5)
	done
	ipkg-cl ${IPKG_ARGS} update
	if [ ! -z "${LINGUAS_INSTALL}" ]; then
		ipkg-cl ${IPKG_ARGS} install glibc-localedata-i18n
		for i in ${LINGUAS_INSTALL}; do
			ipkg-cl ${IPKG_ARGS} install $i
		done
	fi
	if [ ! -z "${PACKAGE_INSTALL}" ]; then
		ipkg-cl ${IPKG_ARGS} install ${PACKAGE_INSTALL}
	fi

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	mkdir -p ${IMAGE_ROOTFS}/etc/ipkg/
	grep "^arch" ${T}/ipkg.conf >${IMAGE_ROOTFS}/etc/ipkg/arch.conf

	for i in ${IMAGE_ROOTFS}${libdir}/ipkg/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
			ipkg-cl ${IPKG_ARGS} flag unpacked `basename $i .preinst`
		fi
	done
	for i in ${IMAGE_ROOTFS}${libdir}/ipkg/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
			ipkg-cl ${IPKG_ARGS} flag unpacked `basename $i .postinst`
		fi
	done

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	${ROOTFS_POSTPROCESS_COMMAND}
	
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
	rm -rf ${IMAGE_ROOTFS}/usr/lib/ipkg/
}
