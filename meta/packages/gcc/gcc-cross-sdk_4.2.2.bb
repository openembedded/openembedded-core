DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"
PR = "r3"

inherit sdk

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"

PACKAGES = "${PN}"

require gcc_${PV}.bb
require gcc4-build-sdk.inc
require gcc-package-sdk.inc

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc gmp-native mpfr-native"

EXTRA_OECONF += "--disable-libunwind-exceptions --disable-libssp \
		--disable-libgomp --disable-libmudflap \
		--with-mpfr=${STAGING_DIR_NATIVE}${layout_exec_prefix}"
