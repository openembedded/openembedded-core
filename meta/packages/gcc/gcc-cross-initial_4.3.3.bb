require gcc-cross_${PV}.bb
require gcc-cross-initial.inc

PR = "r1"

DEPENDS += "gmp-native mpfr-native"

EXTRA_OECONF += " --disable-libmudflap \
		  --disable-decimal-float \
		  --disable-libgomp \
		  --disable-libssp"

# Hack till we fix *libc properly
do_install_append() {
        ln -sf ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include-fixed/* ${D}${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include/
}

