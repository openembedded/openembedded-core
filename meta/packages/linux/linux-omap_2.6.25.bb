require linux-omap.inc

PR = "r1"

DEFAULT_PREFERENCE = "-1"

COMPATIBLE_MACHINE = "omap-3430sdp"
SRC_URI="git://git.kernel.org/pub/scm/linux/kernel/git/tmlind/linux-omap-2.6.git;protocol=git;tag=3ce7ba0c3c9566f50725b0108916180db86e1641 \
         file://defconfig-omap-3430sdp"
S = "${WORKDIR}/git"

