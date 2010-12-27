require linux-omap2.inc

FILESPATH = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/linux-omap2-git/${MACHINE}:${@os.path.dirname(bb.data.getVar('FILE',d,1))}/linux-omap2-git"

PV = "2.6.26"
#PV = "2.6.26+2.6.27-rc1+${PR}+git${SRCPV}"
PR = "r53"

SRC_URI = "git://source.mvista.com/git/linux-omap-2.6.git;protocol=git \
           file://fixes.patch;patch=1 \
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
           file://01-fix-timing-print.diff;patch=1 \
           file://03-enable-overlay-opt.diff;patch=1 \
           file://04-use-pcd.diff;patch=1 \
           file://05-fix-display-panning.diff;patch=1 \
           file://06-ensure-fclk.diff;patch=1 \
           file://07-set-burst-size.diff;patch=1 \
           file://cache-display-fix.patch;patch=1 \
           file://i2c-omap-race-fix.diff;patch=1 \
           file://TWL4030-01.patch;patch=1 \
           file://TWL4030-02.patch;patch=1 \
           file://TWL4030-03.patch;patch=1 \
           file://TWL4030-04.patch;patch=1 \
           file://TWL4030-05.patch;patch=1 \
           file://TWL4030-06.patch;patch=1 \
           file://TWL4030-07.patch;patch=1 \
           file://TWL4030-08.patch;patch=1 \
           file://TWL4030-09.patch;patch=1 \
           file://mru-clocks1.diff;patch=1 \
           file://mru-clocks2.diff;patch=1 \
           file://mru-clocks3.diff;patch=1 \	
           file://4bitmmc.diff;patch=1 \
	   file://400khz-i2c.diff;patch=1 \
           file://no-cortex-deadlock.patch;patch=1 \
           file://01-gptimer_clear_isrs_on_init;patch=1 \
           file://02-gptimer_use_match_for_tick;patch=1 \
           file://03-gptimer_match_plus_ovf;patch=1 \
           file://04-gptimer_add_debug_to_sysrq_q;patch=1 \
           file://read_die_ids.patch;patch=1 \
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
