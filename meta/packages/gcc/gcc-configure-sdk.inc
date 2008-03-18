require gcc-configure-common.inc

# The two lines below conflict, this needs fixing - RP
USE_NLS = '${@base_conditional( "TARGET_OS", "linux-uclibc", "no", "", d )}'
USE_NLS = '${@base_conditional( "TARGET_OS", "linux-uclibcgnueabi", "no", "", d )}'

EXTRA_OECONF_PATHS = "--with-local-prefix=${STAGING_DIR_TARGET}${layout_prefix} \
                      --with-gxx-include-dir=${STAGING_DIR_TARGET}/${layout_includedir}/c++ \
                      --with-sysroot=${prefix}/${TARGET_SYS} \
                      --with-build-sysroot=${STAGING_DIR_TARGET}"

#
# gcc-cross looks and finds these in ${exec_prefix} but we're not so lucky
# for the sdk. Hardcoding the paths ensures the build doesn't go canadian or worse.
#
export AR_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/ar"
export AS_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/as"
export DLLTOOL_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/dlltool"
export LD_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/ld"
export LIPO_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/lipo"
export NM_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/nm"
export OBJDUMP_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/objdump"
export RANLIB_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/ranlib"
export STRIP_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/strip"
export WINDRES_FOR_TARGET = "${CROSS_DIR}/${TARGET_SYS}/bin/windres"

#
# We need to override this and make sure the compiler can find staging
#
export ARCH_FLAGS_FOR_TARGET = "--sysroot=${STAGING_DIR_TARGET}"

do_configure () {
	export CC_FOR_BUILD="${BUILD_CC}"
	export CXX_FOR_BUILD="${BUILD_CXX}"
	export CFLAGS_FOR_BUILD="${BUILD_CFLAGS}"
	export CPPFLAGS_FOR_BUILD="${BUILD_CPPFLAGS}"
	export CXXFLAGS_FOR_BUILD="${BUILD_CXXFLAGS}"
	export LDFLAGS_FOR_BUILD="${BUILD_LDFLAGS}"
	(cd ${S} && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}

do_compile () {
	export CC="${BUILD_CC}"
	export AR_FOR_TARGET="${TARGET_SYS}-ar"
	export RANLIB_FOR_TARGET="${TARGET_SYS}-ranlib"
	export LD_FOR_TARGET="${TARGET_SYS}-ld"
	export NM_FOR_TARGET="${TARGET_SYS}-nm"
	export CC_FOR_TARGET="${CCACHE} ${TARGET_SYS}-gcc ${TARGET_CC_ARCH}"
	base_do_compile
}
