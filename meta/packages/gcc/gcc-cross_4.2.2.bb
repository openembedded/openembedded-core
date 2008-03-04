require gcc_${PV}.bb
# path mangling, needed by the cross packaging
require gcc-paths-cross.inc
inherit cross
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"
# NOTE: split PR.  If the main .oe changes something that affects its *build*
# remember to increment this one too.
PR = "r6"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc gmp-native mpfr-native"
PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

# cross build
require gcc3-build-cross.inc
# cross packaging
require gcc-package-cross.inc
SRC_URI_append_fail-fast = " file://zecke-no-host-includes.patch;patch=1 "
# Do not build libssp libmudflap and libgomp
# We might need them for some beefy targets 
EXTRA_OECONF += "--disable-libunwind-exceptions --disable-libssp \
		--disable-libgomp --disable-libmudflap \
		--with-mpfr=${STAGING_DIR_NATIVE}${layout_exec_prefix}"
