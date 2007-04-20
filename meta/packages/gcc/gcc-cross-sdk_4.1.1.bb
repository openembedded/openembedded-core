DESCRIPTION = "The GNU cc and gcc C compilers."
require gcc_${PV}.bb
PR = "r0"

inherit sdk

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
PACKAGES = "${PN}"

require gcc4-build-sdk.inc
require gcc-package-sdk.inc

SRC_URI = "http://ftp.gnu.org/pub/gnu/gcc/gcc-4.1.1/gcc-4.1.1.tar.bz2 \
	file://100-uclibc-conf.patch;patch=1 \
	file://110-arm-eabi.patch;patch=1 \
	file://200-uclibc-locale.patch;patch=1 \
	file://300-libstdc++-pic.patch;patch=1 \
	file://301-missing-execinfo_h.patch;patch=1 \
	file://302-c99-snprintf.patch;patch=1 \
	file://303-c99-complex-ugly-hack.patch;patch=1 \
	file://304-index_macro.patch;patch=1 \
	file://602-sdk-libstdc++-includes.patch;patch=1 \
	file://740-sh-pr24836.patch;patch=1 \
	file://800-arm-bigendian.patch;patch=1 \
	file://arm-nolibfloat.patch;patch=1 \
	file://arm-softfloat.patch;patch=1 \
	file://gcc41-configure.in.patch;patch=1 \
	file://arm-thumb.patch;patch=1 \
	file://arm-thumb-cache.patch;patch=1 \
	file://ldflags.patch;patch=1 \
	file://cse.patch;patch=1 \
	file://unbreak-armv4t.patch;patch=1 \
        file://fix-ICE-in-arm_unwind_emit_set.diff;patch=1 \
        file://gcc-4.1.1-pr13685-1.patch;patch=1 \
        file://gcc-ignore-cache.patch;patch=1 \
	"
