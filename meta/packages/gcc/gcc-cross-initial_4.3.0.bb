require gcc-cross_${PV}.bb
require gcc-cross-initial.inc

EXTRA_OECONF += "--disable-libmudflap --disable-libgomp \
		--disable-libssp"

DEPENDS += "mpfr-native gmp-native"

# Hack till we fix *libc properly
do_stage_append() {
        ln -sf ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include-fixed/* ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include/
}

