include gcc_${PV}.bb
# path mangling, needed by the cross packaging
include gcc-paths-cross.inc
inherit cross
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"
# NOTE: split PR.  If the main .oe changes something that affects its *build*
# remember to increment this one too.
PR = "r10"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

# cross build
include gcc3-build-cross.inc
# cross packaging
include gcc-package-cross.inc
