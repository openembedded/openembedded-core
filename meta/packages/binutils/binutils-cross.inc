SECTION = "devel"
inherit cross
DEPENDS += "flex-native bison-native"
PROVIDES = "virtual/${TARGET_PREFIX}binutils"
PACKAGES = ""
EXTRA_OECONF = "--with-sysroot=${STAGING_DIR_TARGET} \
		--program-prefix=${TARGET_PREFIX} \
		--enable-install-libbfd \
                --disable-werror"

do_install () {
	oe_runmake 'DESTDIR=${D}' install

	# We don't really need these, so we'll remove them...
	rm -rf ${D}${CROSS_DIR}/lib/ldscripts
	rm -rf ${D}${CROSS_DIR}/share/info
	rm -rf ${D}${CROSS_DIR}/share/locale
	rm -rf ${D}${CROSS_DIR}/share/man
	rmdir ${D}${CROSS_DIR}/share || :
	rmdir ${D}${CROSS_DIR}/${libdir}/gcc-lib || :
	rmdir ${D}${CROSS_DIR}/${libdir}64/gcc-lib || :
	rmdir ${D}${CROSS_DIR}/${libdir} || :
	rmdir ${D}${CROSS_DIR}/${libdir}64 || :
	rmdir ${D}${CROSS_DIR}/${prefix} || :
}

do_stage () {
	use_do_install_for_stage
}
