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

EXTRA_OECONF += "--with-mpfr=${STAGING_DIR}/${BUILD_SYS}"
