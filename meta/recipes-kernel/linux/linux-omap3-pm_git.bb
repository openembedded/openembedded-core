DESCRIPTION = "Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL"

inherit kernel

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/${PN}-git/${MACHINE}"

PV = "2.6.31-rc7+pm+${PR}+git${SRCPV}"
PR = "r3"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/khilman/linux-omap-pm.git;branch=pm;protocol=git"

SRC_URI_append_zoom2 = " \
        file://0001-OMAP1-2-3-4-DEBUG_LL-cleanup.patch;patch=1 \
        file://0002-OMAP1-2-3-4-Adapt-board-files-for-cleand-DEBUG_LL-in.patch;patch=1 \
        file://0003-OMAP-Zoom2-Add-DEBUG_LL-interface-using-external-Qua.patch;patch=1 \
        "

COMPATIBLE_MACHINE = "beagleboard|omap-3430sdp|omap3evm|overo|rx51|zoom2"

S = "${WORKDIR}/git"

do_configure_prepend() {
	# logo support: if you supply logo_linux_clut224.ppm in SRC_URI,
	# then it's going to be used instead of "Tux" in the kernel sources
	if [ -e ${WORKDIR}/logo_linux_clut224.ppm ]; then
		install -m 0644 ${WORKDIR}/logo_linux_clut224.ppm \
			drivers/video/logo/logo_linux_clut224.ppm
	fi

	# use the externally-supplied ${MACHINE}_external_defconfig if present,
	# else use omap3_pm_defconfig from the sources
	if [ -e ${WORKDIR}/${MACHINE}_external_defconfig ]; then
		cp ${WORKDIR}/${MACHINE}_external_defconfig ${S}/arch/arm/configs/
		yes '' | oe_runmake ${MACHINE}_external_defconfig
	else
		case ${MACHINE} in
			omap-3430sdp | omap3evm)
				# works out of the box
				yes '' | oe_runmake omap3_pm_defconfig
				;;
			beagleboard | overo | rx51)
				# adjust LL_DEBUG console for these boards
				yes '' | oe_runmake omap3_pm_defconfig
				sed -e "s/CONFIG_OMAP_LL_DEBUG_UART1=y/# CONFIG_OMAP_LL_DEBUG_UART1 is not set/" \
					-e "s/# CONFIG_OMAP_LL_DEBUG_UART3 is not set/CONFIG_OMAP_LL_DEBUG_UART3=y/" \
					-e "s/CONFIG_MMC_BLOCK=m/CONFIG_MMC_BLOCK=y/" \
					-e "s/CONFIG_MMC_OMAP_HS=m/CONFIG_MMC_OMAP_HS=y/" \
					-i ${S}/.config
				;;
			zoom2)
				# adjust LL_DEBUG console for this board
				yes '' | oe_runmake omap3_pm_defconfig
				sed -e "s/CONFIG_OMAP_LL_DEBUG_UART1=y/# CONFIG_OMAP_LL_DEBUG_UART1 is not set/" \
					-i ${S}/.config
				echo "CONFIG_OMAP_LL_DEBUG_UART_EXT=y" >> ${S}/.config
				;;
			*)
				# its worth a try...
				yes '' | oe_runmake ${MACHINE}_defconfig
				;;
		esac
	fi
}
