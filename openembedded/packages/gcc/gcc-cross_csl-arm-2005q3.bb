include gcc_csl-arm-2005q3.bb
# path mangling, needed by the cross packaging
include gcc-paths-cross.inc
inherit cross
# NOTE: split PR.  If the main .oe changes something that affects its *build*
# remember to increment this one too.
PR = "r0"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

# cross build
include gcc3-cross-build.inc
# cross packaging
include gcc-package-cross.inc
