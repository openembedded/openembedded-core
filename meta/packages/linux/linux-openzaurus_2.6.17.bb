include linux-openzaurus.inc

PR = "r12"

# Handy URLs
# git://rsync.kernel.org/pub/scm/linux/kernel/git/torvalds/linux-2.6.git \
# http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.14.tar.gz \
# http://www.kernel.org/pub/linux/kernel/v2.6/testing/patch-2.6.15-rc1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/v2.6/snapshots/patch-2.6.15-rc2-git1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/alan/linux-2.6/2.6.10/patch-2.6.10-ac8.gz;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/akpm/patches/2.6/2.6.14-rc2/2.6.14-rc2-mm1/2.6.14-rc2-mm1.bz2;patch=1 \	   

# Patches submitted upstream are towards top of this list 
# Hacks should clearly named and at the bottom
#           ftp://ftp.kernel.org/pub/linux/kernel/people/akpm/patches/2.6/2.6.14-rc2/2.6.14-rc2-mm1/2.6.14-rc2-mm1.bz2;patch=1 \	   
SRC_URI = "http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.17.tar.bz2 \
           ${RPSRC}/poodle_partsize-r0.patch;patch=1;status=merged \
           ${RPSRC}/jffs2_longfilename-r1.patch;patch=1;status=merged \
           ${RPSRC}/locomo_led_default_trigger-r0.patch;patch=1;status=merged \
           ${RPSRC}/zaurus_reboot-r3.patch;patch=1;status=merged \
           ${RPSRC}/poodle_mmcsd_fix-r0.patch;patch=1;status=merged \
           ${RPSRC}/poodle_ssp-r1.patch;patch=1;status=merged \
           ${RPSRC}/sharpsl_pm-do-r2.patch;patch=1;status=merged \
           ${RPSRC}/zlib_inflate-r3.patch;patch=1;status=merged \
           ${RPSRC}/logo_rotate_fix-r1.patch;patch=1;status=merged \
           ${RPSRC}/collie_frontlight-r6.patch;patch=1;status=merged \
           ${RPSRC}/input_modalias_fix-r0.patch;patch=1;status=merged \
           file://00-hostap.patch;patch=1;status=merged \
           file://10-pcnet.patch;patch=1;status=merged \
           ${RPSRC}/asoc-v0.11pre5-oz.patch;patch=1 \
           ${RPSRC}/asoc_fixes2-r1.patch;patch=1 \
           ${RPSRC}/hx2750_base-r27.patch;patch=1 \
           ${RPSRC}/hx2750_bl-r7.patch;patch=1 \
           ${RPSRC}/hx2750_pcmcia-r2.patch;patch=1 \
           ${RPSRC}/pxa_keys-r5.patch;patch=1 \
           ${RPSRC}/tsc2101-r12.patch;patch=1 \
           ${RPSRC}/hx2750_test1-r4.patch;patch=1 \
           ${RPSRC}/pxa_timerfix-r0.patch;patch=1 \
           ${RPSRC}/input_power-r5.patch;patch=1 \
           ${RPSRC}/pxa25x_cpufreq-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_pm_fixes1-r0.patch;patch=1 \
           ${RPSRC}/pm_changes-r1.patch;patch=1 \
           ${RPSRC}/usb_pxa27x_udc-r0.patch;patch=1 \
           ${RPSRC}/usb_add_epalloc-r1.patch;patch=1 \
           ${DOSRC}/kexec-arm-r3.patch;patch=1 \
           ${RPSRC}/locomo_kbd_tweak-r0.patch;patch=1 \
           ${RPSRC}/poodle_pm-r1.patch;patch=1 \
           ${RPSRC}/pxafb_changeres-r0.patch;patch=1 \
           ${RPSRC}/poodle_audio-r1.patch;patch=1 \
           file://serial-add-support-for-non-standard-xtals-to-16c950-driver.patch;patch=1 \
           file://hrw-pcmcia-ids-r3.patch;patch=1 \
           ${RPSRC}/logo_oh-r0.patch.bz2;patch=1;status=unmergable \
           ${RPSRC}/logo_oz-r2.patch.bz2;patch=1;status=unmergable \
           ${RPSRC}/pxa-linking-bug.patch;patch=1;status=unmergable \
           file://add-oz-release-string.patch;patch=1;status=unmergable \
           ${RPSRC}/mmcsd_large_cards-r0.patch;patch=1;status=hack \
           ${RPSRC}/mmcsd_no_scr_check-r0.patch;patch=1;status=hack \
           ${RPSRC}/integrator_rgb-r0.patch;patch=1;status=hack \
           ${RPSRC}/pxa_cf_initorder_hack-r1.patch;patch=1;status=hack \
           file://pxa-serial-hack.patch;patch=1;status=hack \
           file://connectplus-remove-ide-HACK.patch;patch=1;status=hack \
           file://squashfs3.0-2.6.15.patch;patch=1;status=external \
           file://defconfig-c7x0 \
           file://defconfig-ipaq-pxa270 \
           file://defconfig-collie \
           file://defconfig-poodle \
           file://defconfig-akita \
           file://defconfig-spitz \
           file://defconfig-qemuarm \
           file://defconfig-tosa "

# Add this to enable pm debug code (useful with a serial lead)
#  ${RPSRC}/sharpsl_pm_debug-r0.patch;patch=1

# Disabled until I find the reason this gives issues with cdc_subset
#            ${RPSRC}/usb_rndis_tweaks-r0.patch;patch=1 \

#           http://tglx.de/projects/armirq/2.6.17-rc3/patch-2.6.17-rc3-armirq4.patch;patch=1 \
#           ${RPSRC}/../pxa27x_overlay-r0.patch;patch=1 \

# Is anything out of this still needed? Parts were commited to mainline by rmk (drivers/mfd/)
# (Pavel Machek's git tree has updated versions of this?)
#  ${JLSRC}/zaurus-lcd-2.6.11.diff.gz;patch=1 

# These patches are extracted from Pavel Machek's git tree
# (diff against vanilla kernel)
SRC_URI_append_collie = "\
	   ${DOSRC}/collie/mtd-sharp-flash-hack-r0.patch;patch=1 \
	   ${DOSRC}/collie/collie-r0.patch;patch=1 \
	   ${DOSRC}/collie/locomolcd-backlight-r0.patch;patch=1 \
	   ${DOSRC}/collie/ucb1x00-touch-audio-r0.patch;patch=1 \
	   ${DOSRC}/collie/collie-mcp-r0.patch;patch=1 \
	   ${DOSRC}/collie/sa1100-udc-r0.patch;patch=1 \
#	   ${DOSRC}/collie/collie-pm-r1.patch;patch=1 \
	   "

SRC_URI_append_tosa = "\
	   ${CHSRC}/usb-ohci-hooks-r1.patch;patch=1 \
	   ${CHSRC}/tmio-core-r4.patch;patch=1 \
	   ${DOSRC}/temp/tmio-tc6393-r6.patch;patch=1 \
	   ${CHSRC}/tmio-nand-r5.patch;patch=1 \
	   ${DOSRC}/temp/tmio-ohci-r4.patch;patch=1 \
	   ${CHSRC}/tmio-fb-r6.patch;patch=1 \
	   ${DOSRC}/tosa-keyboard-r17.patch;patch=1 \
	   ${DOSRC}/tosa-pxaac97-r6.patch;patch=1 \
	   ${DOSRC}/tosa-tmio-r6.patch;patch=1 \
	   ${DOSRC}/tosa-power-r17.patch;patch=1 \
	   ${DOSRC}/tosa-tmio-lcd-r8.patch;patch=1 \
	   ${DOSRC}/tosa-bluetooth-r8.patch;patch=1 \
	   ${DOSRC}/wm97xx-lg7-r0.patch;patch=1 \
	   ${DOSRC}/wm9712-suspend-cold-res-r1.patch;patch=1 \
	   ${DOSRC}/sharpsl-pm-postresume-r0.patch;patch=1 \
	   ${DOSRC}/wm97xx-dig-restore-r0.patch;patch=1 \
	   ${DOSRC}/wm97xx-miscdevs-resume-r0.patch;patch=1 \
	   ${DOSRC}/wm9712-reset-loop-r1.patch;patch=1"
#	   ${DOSRC}/tosa-asoc-r1.patch;patch=1 "

S = "${WORKDIR}/linux-2.6.17"

# to get module dependencies working
KERNEL_RELEASE = "2.6.17"
