require gcc-cross_${PV}.bb
require gcc-cross-intermediate.inc

DEPENDS += "gmp-native mpfr-native"

EXTRA_OECONF += " --disable-libmudflap \
		  --disable-libgomp \
		  --disable-libssp"

# Hack till we fix *libc properly
do_stage_append() {
        ln -sf ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include-fixed/* ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include/
}

