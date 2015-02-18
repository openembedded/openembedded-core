INHIBIT_DEFAULT_DEPS = "1"
LICENSE = "GPLv2 & LGPLv2.1"
HOMEPAGE = "http://www.gnu.org/software/libc/index.html"

# This needs to match with glibc.inc, otherwise glibc-scripts and glibc-locale
# will fail to find main glibc, for details see
# http://lists.openembedded.org/pipermail/openembedded-core/2015-January/100679.html
ARM_INSTRUCTION_SET = "arm"

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install[depends] += "virtual/${MLPREFIX}libc:do_populate_sysroot"

