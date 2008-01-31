DESCRIPTION = "The GNU cc and gcc C compilers."
HOMEPAGE = "http://www.gnu.org/software/gcc/"
SECTION = "devel"
LICENSE = "GPL"
require gcc_${PV}.bb
PR = "r3"

inherit sdk

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gcc-${PV}"

DEPENDS = "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}libc-for-gcc"
PACKAGES = "${PN}"

require gcc3-build-sdk.inc
require gcc-package-sdk.inc
