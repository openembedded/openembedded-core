require gcc_csl-arm-2006q1.bb
# path mangling, needed by the cross packaging
require gcc-paths-cross.inc
inherit cross
# NOTE: split PR.  If the main .oe changes something that affects its *build*
# remember to increment this one too.
PR = "r0"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
# gmp-native mpfr-native"

PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

# cross build
require gcc4-build-cross.inc
# cross packaging
require gcc-package-cross-no-fortran.inc

S = "${WORKDIR}/gcc-2006q1"
