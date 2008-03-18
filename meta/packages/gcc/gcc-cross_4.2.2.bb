PR = "r6"

require gcc-${PV}.inc
require gcc-cross4.inc
require gcc-configure-cross.inc
require gcc-package-cross.inc

SRC_URI_append_fail-fast = " file://zecke-no-host-includes.patch;patch=1 "
# Do not build libssp libmudflap and libgomp
# We might need them for some beefy targets 
EXTRA_OECONF += "--disable-libunwind-exceptions --disable-libssp \
		--disable-libgomp --disable-libmudflap \
		--with-mpfr=${STAGING_DIR_NATIVE}${layout_exec_prefix}"

ARCH_FLAGS_FOR_TARGET += "-isystem${STAGING_DIR_TARGET}${layout_includedir}"
