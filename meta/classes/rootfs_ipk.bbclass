#
# Creates a root filesystem out of IPKs
#
# This rootfs can be mounted via root-nfs or it can be put into an cramfs/jffs etc.
# See image.bbclass for a usage of this.
#

EXTRAOPKGCONFIG ?= ""
ROOTFS_PKGMANAGE = "opkg opkg-collateral ${EXTRAOPKGCONFIG}"
ROOTFS_PKGMANAGE_BOOTSTRAP  = "run-postinsts"

do_rootfs[depends] += "opkg-native:do_populate_sysroot opkg-utils-native:do_populate_sysroot"
do_rootfs[recrdeptask] += "do_package_write_ipk"

IPKG_ARGS = "-f ${IPKGCONF_TARGET} -o ${IMAGE_ROOTFS} --force-overwrite"

OPKG_PREPROCESS_COMMANDS = "package_update_index_ipk; package_generate_ipkg_conf"

OPKG_POSTPROCESS_COMMANDS = "ipk_insert_feed_uris"

opkglibdir = "${localstatedir}/lib/opkg"

fakeroot rootfs_ipk_do_rootfs () {
	set -x

	rm -f ${IPKGCONF_TARGET}
	touch ${IPKGCONF_TARGET}

	${OPKG_PREPROCESS_COMMANDS}

	mkdir -p ${T}/

	#install
	export INSTALL_PACKAGES_ATTEMPTONLY_IPK="${PACKAGE_INSTALL_ATTEMPTONLY}"
	export INSTALL_PACKAGES_LINGUAS_IPK="${LINGUAS_INSTALL}"
	export INSTALL_TASK_IPK="rootfs"

	export INSTALL_ROOTFS_IPK="${IMAGE_ROOTFS}"
	export INSTALL_CONF_IPK="${IPKGCONF_TARGET}"
	export INSTALL_PACKAGES_NORMAL_IPK="${PACKAGE_INSTALL}"

	package_install_internal_ipk

	#post install
	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IPKG_OFFLINE_ROOT}

	# Distro specific packages should create this
	#mkdir -p ${IMAGE_ROOTFS}/etc/opkg/
	#grep "^arch" ${IPKGCONF_TARGET} >${IMAGE_ROOTFS}/etc/opkg/arch.conf

	${OPKG_POSTPROCESS_COMMANDS}
	${ROOTFS_POSTINSTALL_COMMAND}
	
	runtime_script_required=0
	for i in ${IMAGE_ROOTFS}${opkglibdir}/info/*.preinst; do
		if [ -f $i ] && ! sh $i; then
		     	runtime_script_required=1
			opkg-cl ${IPKG_ARGS} flag unpacked `basename $i .preinst`
		fi
	done
	for i in ${IMAGE_ROOTFS}${opkglibdir}/info/*.postinst; do
		if [ -f $i ] && ! sh $i configure; then
		     	runtime_script_required=1
			opkg-cl ${IPKG_ARGS} flag unpacked `basename $i .postinst`
		fi
	done

	if ${@base_contains("IMAGE_FEATURES", "read-only-rootfs", "true", "false" ,d)}; then
		if [ $runtime_script_required -eq 1 ]; then
			echo "Some packages could not be configured offline and rootfs is read-only."
			exit 1
		fi
	fi

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	${ROOTFS_POSTPROCESS_COMMAND}
	
	rm -f ${IMAGE_ROOTFS}${opkglibdir}/lists/*

	log_check rootfs 	
}

rootfs_ipk_write_manifest() {
	manifest=${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.manifest
	cp ${IMAGE_ROOTFS}${opkglibdir}/status $manifest

	sed '/Depends/d' -i $manifest
	sed '/Status/d' -i $manifest
	sed '/Architecture/d' -i $manifest
	sed '/Installed-Time/d' -i $manifest
	sed '/Auto-Installed/d' -i $manifest
	sed '/Recommends/d' -i $manifest
	sed '/Provides/d' -i $manifest
	sed '/Conflicts/d' -i $manifest
}

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
        # We need the directory for the package manager lock
        mkdir ${IMAGE_ROOTFS}${opkglibdir}
}

install_all_locales() {

    PACKAGES_TO_INSTALL=""

    INSTALLED_PACKAGES=`grep ^Package: ${IMAGE_ROOTFS}${opkglibdir}/status |sed "s/^Package: //"|egrep -v -- "(-locale-|-dev$|-doc$|^kernel|^glibc|^ttf|^task|^perl|^python)"`

    for pkg in $INSTALLED_PACKAGES
    do
        for lang in ${IMAGE_LOCALES}
        do
            if [ `opkg-cl ${IPKG_ARGS} info $pkg-locale-$lang | wc -l` -gt 2 ]
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

ipk_insert_feed_uris () {

	echo "Building from feeds activated!"

	for line in ${IPK_FEED_URIS}
	do
		# strip leading and trailing spaces/tabs, then split into name and uri
		line_clean="`echo "$line"|sed 's/^[ \t]*//;s/[ \t]*$//'`"
		feed_name="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\1/p'`"
		feed_uri="`echo "$line_clean" | sed -n 's/\(.*\)##\(.*\)/\2/p'`"

		echo "Added $feed_name feed with URL $feed_uri"

		# insert new feed-sources
		echo "src/gz $feed_name $feed_uri" >> ${IPKGCONF_TARGET}
        done
}

python () {

    if bb.data.getVar('BUILD_IMAGES_FROM_FEEDS', d, True):
        flags = bb.data.getVarFlag('do_rootfs', 'recrdeptask', d)
        flags = flags.replace("do_package_write_ipk", "")
        flags = flags.replace("do_deploy", "")
        flags = flags.replace("do_populate_sysroot", "")
        bb.data.setVarFlag('do_rootfs', 'recrdeptask', flags, d)
        bb.data.setVar('OPKG_PREPROCESS_COMMANDS', "package_generate_archlist\nipk_insert_feed_uris", d)
        bb.data.setVar('OPKG_POSTPROCESS_COMMANDS', '', d)
}

