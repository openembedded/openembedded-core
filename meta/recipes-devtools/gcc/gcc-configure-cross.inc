require gcc-configure-common.inc

EXTRA_OECONF += " --enable-poison-system-directories \
		"

INHIBIT_DEFAULT_DEPS = "1"

EXTRA_OECONF_PATHS = " \
		      --with-headers=${STAGING_DIR_TARGET}${SYSTEMHEADERS} \
		      --with-gxx-include-dir=${STAGING_DIR_TARGET}${target_includedir}/c++ \
                      --with-sysroot=${STAGING_DIR_TARGET} \
                      --with-build-sysroot=${STAGING_DIR_TARGET}"

do_configure_prepend () {
	sed -i 's/BUILD_INFO=info/BUILD_INFO=/' ${S}/gcc/configure
}
do_compile_prepend () {
	export CC="${BUILD_CC}"
	export AR_FOR_TARGET="${TARGET_SYS}-ar"
	export RANLIB_FOR_TARGET="${TARGET_SYS}-ranlib"
	export LD_FOR_TARGET="${TARGET_SYS}-ld"
	export NM_FOR_TARGET="${TARGET_SYS}-nm"
	export CC_FOR_TARGET="${CCACHE} ${TARGET_SYS}-gcc ${TARGET_CC_ARCH}"
	export CFLAGS_FOR_TARGET="${TARGET_CFLAGS}"
	export CPPFLAGS_FOR_TARGET="${TARGET_CPPFLAGS}"
	export CXXFLAGS_FOR_TARGET="${TARGET_CXXFLAGS}"
	export LDFLAGS_FOR_TARGET="${TARGET_LDFLAGS}"
}

LIBGCCS_VAR = "-lgcc_s"
LIBGCCS_VAR_avr32 = ""
