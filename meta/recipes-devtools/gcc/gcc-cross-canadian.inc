inherit cross-canadian

DESCRIPTION = "The cross-canadian GNU cc and gcc C compilers for ${TARGET_ARCH} target."
PN = "gcc-cross-canadian-${TRANSLATED_TARGET_ARCH}"
BPN = "gcc"

DEPENDS = "virtual/${TARGET_PREFIX}gcc virtual/${HOST_PREFIX}binutils-crosssdk virtual/nativesdk-${HOST_PREFIX}libc-for-gcc nativesdk-gettext"

GCCMULTILIB = "--enable-multilib"

require gcc-configure-sdk.inc
require gcc-package-sdk.inc

ELFUTILS = "nativesdk-elfutils"
DEPENDS += "nativesdk-gmp nativesdk-mpfr nativesdk-libmpc ${ELFUTILS} nativesdk-zlib"
RDEPENDS_${PN} += "nativesdk-mpfr nativesdk-libmpc ${ELFUTILS}"

SYSTEMHEADERS = "/usr/include"
SYSTEMLIBS = "${target_base_libdir}/"
SYSTEMLIBS1 = "${target_libdir}/"

EXTRA_OECONF += "--disable-libunwind-exceptions --disable-libssp \
		--disable-libgomp --disable-libmudflap \
		--with-mpfr=${STAGING_DIR_HOST}${layout_exec_prefix} \
		--with-mpc=${STAGING_DIR_HOST}${layout_exec_prefix}"

# gcc 4.7 needs -isystem
export ARCH_FLAGS_FOR_TARGET = "--sysroot=${STAGING_DIR_TARGET} -isystem=${target_includedir}"
