do_populate_sdk[depends] += "opkg-native:do_populate_sysroot opkg-utils-native:do_populate_sysroot"
do_populate_sdk[recrdeptask] += "do_package_write_ipk"

do_populate_sdk[lockfiles] += "${WORKDIR}/ipk.lock"

list_installed_packages() {
	if [ "$1" = "arch" ] ; then
		opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py -a
	elif [ "$1" = "file" ] ; then
		opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py -f | while read pkg pkgfile pkgarch
		do
			fullpath=`find ${DEPLOY_DIR_IPK} -name "$pkgfile" || true`
			if [ "$fullpath" = "" ] ; then
				echo "$pkg $pkgfile $pkgarch"
			else
				echo "$pkg $fullpath $pkgarch"
			fi
		done
	elif [ "$1" = "ver" ] ; then
		opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py -v
	else
		opkg-cl ${OPKG_ARGS} list_installed | awk '{ print $1 }'
	fi
}

rootfs_list_installed_depends() {
	opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py
}
