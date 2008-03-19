SECTION = "devel"
inherit cross
DEPENDS += "flex-native bison-native"
PROVIDES = "virtual/${TARGET_PREFIX}binutils"
PACKAGES = ""
EXTRA_OECONF = "--with-sysroot=${STAGING_DIR_TARGET} \
		--program-prefix=${TARGET_PREFIX} \
		--enable-install-libbfd \
                --disable-werror"

do_stage () {
	oe_runmake install

	# We don't really need these, so we'll remove them...
	rm -rf ${CROSS_DIR}/lib/ldscripts
	rm -rf ${CROSS_DIR}/share/info
	rm -rf ${CROSS_DIR}/share/locale
	rm -rf ${CROSS_DIR}/share/man
	rmdir ${CROSS_DIR}/share || :
	rmdir ${CROSS_DIR}/${libdir}/gcc-lib || :
	rmdir ${CROSS_DIR}/${libdir}64/gcc-lib || :
	rmdir ${CROSS_DIR}/${libdir} || :
	rmdir ${CROSS_DIR}/${libdir}64 || :
	rmdir ${CROSS_DIR}/${prefix} || :

	# We want to move this into the target specific location
	mkdir -p ${STAGING_DIR_TARGET}/lib
	mv -f ${CROSS_DIR}/lib/libiberty.a ${STAGING_DIR_TARGET}/lib/lib || \
		mv -f ${CROSS_DIR}/lib64/libiberty.a ${STAGING_DIR_TARGET}/lib/lib
	rmdir ${CROSS_DIR}/lib || :
	rmdir ${CROSS_DIR}/lib64 || :
}

do_install () {
	:
}
