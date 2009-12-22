inherit cross-canadian

require gcc-${PV}.inc
require gcc-cross-canadian.inc
require gcc-configure-sdk.inc
require gcc-package-sdk.inc

PR = "r8"

DEPENDS += "gmp-nativesdk mpfr-nativesdk"
RDEPENDS_${PN} += "mpfr-nativesdk"

SYSTEMHEADERS = "/usr/include"
SYSTEMLIBS1 = "/usr/lib/"

EXTRA_OECONF += "--disable-libunwind-exceptions --disable-libssp \
		--disable-libgomp --disable-libmudflap \
		--with-mpfr=${STAGING_DIR_HOST}${layout_exec_prefix}"

# to find libmpfr
# export LD_LIBRARY_PATH = "{STAGING_DIR_HOST}${layout_exec_prefix}"

PARALLEL_MAKE = ""
