USE_NLS = '${@base_conditional( "TARGET_OS", "linux-uclibc", "no", "", d )}'
USE_NLS = '${@base_conditional( "TARGET_OS", "linux-uclibcgnueabi", "no", "", d )}'

SYSROOT = "${WORKDIR}/sysroot"
EXTRA_OECONF += "--with-sysroot=${SYSROOT}"

do_configure () {
       # Work around Hardcoded path assumptions in gcc
       mkdir -p ${SYSROOT}/usr/
       ln -s ${STAGING_DIR}/${TARGET_SYS}/include ${SYSROOT}/usr/include
       ln -s ${STAGING_DIR}/${TARGET_SYS}/lib ${SYSROOT}/usr/lib
       (cd ${S} && gnu-configize) || die "failure running gnu-configize"
       (cd ${S}/libstdc++-v3 && autoreconf)
       oe_runconf
}

do_compile () {
	export CC="${BUILD_CC}"
	export AR_FOR_TARGET="${TARGET_SYS}-ar"
	export RANLIB_FOR_TARGET="${TARGET_SYS}-ranlib"
	export LD_FOR_TARGET="${TARGET_SYS}-ld"
	export NM_FOR_TARGET="${TARGET_SYS}-nm"
	export CC_FOR_TARGET="${CCACHE} ${TARGET_SYS}-gcc ${TARGET_CC_ARCH}"
	oe_runmake
}
