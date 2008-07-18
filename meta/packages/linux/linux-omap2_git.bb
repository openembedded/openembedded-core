require linux-omap2.inc

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/linux-omap2-git/${MACHINE}"

SRCREV = "c32c81d59d2d8a66e63f82c9732db256d302068e"

PV = "2.6.25+2.6.26-rc8+${PR}+git${SRCREV}"
PR = "r38"

SRC_URI = "git://source.mvista.com/git/linux-omap-2.6.git;protocol=git \
	   file://defconfig"

SRC_URI_append_beagleboard = " file://no-harry-potter.diff;patch=1 \
           file://0001-ASoC-OMAP-Add-basic-support-for-OMAP34xx-in-McBSP.patch;patch=1 \
           file://flash.patch;patch=1 \
           file://0001-omap3-cpuidle.patch;patch=1 \ 
           file://0002-omap3-cpuidle.patch;patch=1 \
           file://timer-suppression.patch;patch=1 \
           file://soc.patch;patch=1 \
           file://16bpp.patch;patch=1 \
           file://no-empty-flash-warnings.patch;patch=1 \
           file://logo_linux_clut224.ppm \
           file://oprofile-0.9.3.armv7.diff;patch=1 \
"

SRC_URI_append_omap3evm = " file://no-harry-potter.diff;patch=1 \
           file://0001-ASoC-OMAP-Add-basic-support-for-OMAP34xx-in-McBSP.patch;patch=1 \
           file://0001-omap3-cpuidle.patch;patch=1 \
           file://0002-omap3-cpuidle.patch;patch=1 \
           file://timer-suppression.patch;patch=1 \
           file://soc.patch;patch=1 \
           file://no-empty-flash-warnings.patch;patch=1 \
           file://touchscreen.patch;patch=1 \
"


COMPATIBLE_MACHINE = "omap2430sdp|omap2420h4|beagleboard|omap3evm"


S = "${WORKDIR}/git"
