SECTION = "devel"
include gcc_${PV}.bb
include gcc-paths-cross.inc
inherit cross
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"

PR="r3"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
PROVIDES = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

include gcc3-build-cross.inc
include gcc-package-cross.inc