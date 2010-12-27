require linux-omap2.inc

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/linux-omap3-git/${MACHINE}"

PV = "2.6.27-rc6+${PR}+git${SRCPV}"
PR = "r30"

SRC_URI = "git://source.mvista.com/git/linux-omap-2.6.git;branch=master;protocol=git \
	   file://defconfig"

SRC_URI_append_beagleboard = " \
           file://no-empty-flash-warnings.patch;patch=1 \
          "

SRC_URI_append_omap3evm = " \
           file://no-empty-flash-warnings.patch;patch=1 \
          "

SRC_URI_append_overo = " \
           file://logo_linux_clut224.ppm \
           file://no-empty-flash-warnings.patch;patch=1 \
           file://overo.patch;patch=1 \
          "

COMPATIBLE_MACHINE = "beagleboard|omap3evm|overo"

S = "${WORKDIR}/git"
