USE_NLS = '${@base_conditional( "TARGET_OS", "linux-uclibc", "no", "", d )}'

SRC_URI += 'file://sdk-libstdc++-includes.patch;patch=1'

STAGING_TARGET_INCDIR = "${STAGING_DIR}/${TARGET_SYS}/include"
STAGING_TARGET_LIBDIR = "${STAGING_DIR}/${TARGET_SYS}/lib"

do_configure () {
       (cd ${S} && gnu-configize) || die "failure running gnu-configize"
       (cd ${S}/libstdc++-v3 && autoreconf)
       oe_runconf
       mkdir -p gcc
       ln -sf ${CROSS_DIR}/bin/${TARGET_PREFIX}as gcc/as
       ln -sf ${CROSS_DIR}/bin/${TARGET_PREFIX}ld gcc/ld
       ln -sf ${STAGING_TARGET_INCDIR}/* ${S}/include
       ln -sf ${STAGING_TARGET_LIBDIR}/crt*.o gcc/
}

do_compile () {
	export CC="${BUILD_CC}"
	export AR_FOR_TARGET="${TARGET_SYS}-ar"
	export RANLIB_FOR_TARGET="${TARGET_SYS}-ranlib"
	export LD_FOR_TARGET="${TARGET_SYS}-ld"
	export NM_FOR_TARGET="${TARGET_SYS}-nm"
	export CC_FOR_TARGET="${CCACHE} ${TARGET_SYS}-gcc ${TARGET_CC_ARCH}"
	oe_runmake CFLAGS_FOR_TARGET="-I${STAGING_TARGET_INCDIR}"
}
