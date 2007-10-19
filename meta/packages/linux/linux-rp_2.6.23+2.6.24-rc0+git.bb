require linux-rp.inc

PR = "r1"

DEFAULT_PREFERENCE = "-1"

# Handy URLs
# git://git.kernel.org/pub/scm/linux/kernel/git/torvalds/linux-2.6.git;protocol=git;tag=ef7d1b244fa6c94fb76d5f787b8629df64ea4046
# http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.18.tar.bz2
# http://www.kernel.org/pub/linux/kernel/v2.6/testing/linux-2.6.20-rc4.tar.bz2
# http://www.kernel.org/pub/linux/kernel/v2.6/testing/patch-2.6.18-rc6.bz2;patch=1
# http://www.kernel.org/pub/linux/kernel/v2.6/snapshots/patch-2.6.18-rc2-git1.bz2;patch=1
# http://www.kernel.org/pub/linux/kernel/people/alan/linux-2.6/2.6.10/patch-2.6.10-ac8.gz;patch=1
# http://www.kernel.org/pub/linux/kernel/people/akpm/patches/2.6/2.6.14-rc2/2.6.14-rc2-mm1/2.6.14-rc2-mm1.bz2;patch=1

# Patches submitted upstream are towards top of this list 
# Hacks should clearly named and at the bottom
SRC_URI = "http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.23.tar.bz2 \
           http://www.kernel.org/pub/linux/kernel/v2.6/snapshots/patch-2.6.23-git9.bz2;patch=1 \
           ${RPSRC}/lzo_crypto-r2.patch;patch=1 \
           ${RPSRC}/lzo_jffs2_sysfs-r1.patch;patch=1 \
           ${RPSRC}/hx2750_base-r30.patch;patch=1 \
           ${RPSRC}/hx2750_bl-r9.patch;patch=1 \
           ${RPSRC}/hx2750_pcmcia-r3.patch;patch=1 \
           ${RPSRC}/pxa_keys-r8.patch;patch=1 \
           ${RPSRC}/tsc2101-r16.patch;patch=1 \
           ${RPSRC}/hx2750_test1-r7.patch;patch=1 \
           ${RPSRC}/input_power-r9.patch;patch=1 \
           ${RPSRC}/pxa25x_cpufreq-r2.patch;patch=1 \
           ${RPSRC}/sharpsl_pm_fixes1-r0.patch;patch=1 \
           ${RPSRC}/pm_changes-r1.patch;patch=1 \
           ${RPSRC}/usb_add_epalloc-r4.patch;patch=1 \
           ${RPSRC}/usb_pxa27x_udc-r7.patch;patch=1 \
           ${RPSRC}/locomo_kbd_tweak-r1.patch;patch=1 \
           ${RPSRC}/poodle_pm-r5.patch;patch=1 \
           ${RPSRC}/pxa27x_overlay-r7.patch;patch=1 \
           ${RPSRC}/w100_extaccel-r1.patch;patch=1 \
           ${RPSRC}/w100_extmem-r1.patch;patch=1 \
           file://w100fb-unused-var.patch;patch=1 \
           file://hostap-monitor-mode.patch;patch=1 \
           file://serial-add-support-for-non-standard-xtals-to-16c950-driver.patch;patch=1 \
           ${RPSRC}/logo_oh-r1.patch.bz2;patch=1;status=unmergable \
           ${RPSRC}/logo_oz-r2.patch.bz2;patch=1;status=unmergable \
           ${RPSRC}/pxa-linking-bug.patch;patch=1;status=unmergable \
           ${RPSRC}/mmcsd_large_cards-r1.patch;patch=1;status=hack \
           file://mmcsd_no_scr_check-r2.patch;patch=1 \
           ${RPSRC}/integrator_rgb-r1.patch;patch=1;status=hack \
           ${RPSRC}/pxa_cf_initorder_hack-r1.patch;patch=1;status=hack \
           ${RPSRC}/corgi_rearrange_lcd-r0.patch;patch=1 \
           file://pxa-serial-hack.patch;patch=1;status=hack \
           file://connectplus-remove-ide-HACK.patch;patch=1;status=hack \
           file://squashfs3.0-2.6.15.patch;patch=1;status=external \
           file://uvesafb-0.1-rc3-2.6.22.patch;patch=1;status=external \
#           file://htcuni.patch;patch=1 \
           file://binutils-buildid-arm.patch;patch=1 \
           file://defconfig-c7x0 \
           file://defconfig-hx2000 \
           file://defconfig-collie \
           file://defconfig-poodle \
           file://defconfig-akita \
           file://defconfig-spitz \
           file://defconfig-qemuarm \
           file://defconfig-qemux86 \
           file://defconfig-bootcdx86 \
           file://defconfig-htcuniversal \
           file://defconfig-zylonite \
           file://defconfig-tosa "

# FIXMEs before made default	   
# ${RPSRC}/mmcsd_no_scr_check-r1.patch;patch=1;status=hack


# Add this to enable pm debug code (useful with a serial lead)
#  ${RPSRC}/sharpsl_pm_debug-r0.patch;patch=1

# Disabled until I find the reason this gives issues with cdc_subset
#            ${RPSRC}/usb_rndis_tweaks-r0.patch;patch=1 \

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
           file://collie-mcp-r1.patch;patch=1 \
           ${DOSRC}/collie/sa1100-udc-r0.patch;patch=1 \
#          ${DOSRC}/collie/collie-pm-r1.patch;patch=1 \
"

SRC_URI_append_tosa = "\
           ${CHSRC}/usb-ohci-hooks-r1.patch;patch=1 \
           ${CHSRC}/tmio-core-r4.patch;patch=1 \
           file://tmio-tc6393-r8.patch;patch=1 \
           file://tmio-nand-r7.patch;patch=1 \
           file://tmio-ohci-r6.patch;patch=1 \
           ${CHSRC}/tmio-fb-r6.patch;patch=1 \
           file://tosa-keyboard-r18.patch;patch=1 \
           ${DOSRC}/tosa-pxaac97-r6.patch;patch=1 \
           ${DOSRC}/tosa-tmio-r6.patch;patch=1 \
           ${DOSRC}/tosa-power-r17.patch;patch=1 \
           file://tosa-tmio-lcd-r10.patch;patch=1 \
           ${DOSRC}/tosa-bluetooth-r8.patch;patch=1 \
           ${DOSRC}/wm97xx-lg7-r0.patch;patch=1 \
           file://wm9712-suspend-cold-res-r2.patch;patch=1 \
           file://sharpsl-pm-postresume-r1.patch;patch=1 \
           ${DOSRC}/wm97xx-dig-restore-r0.patch;patch=1 \
           ${DOSRC}/wm97xx-miscdevs-resume-r0.patch;patch=1 \
           file://wm9712-reset-loop-r2.patch;patch=1 \
           file://tosa-lcdnoise-r1.patch;patch=1 \
           file://wm97xx-lcdnoise-r0.patch;patch=1 "
#          ${DOSRC}/tosa-asoc-r1.patch;patch=1 "

SRC_URI_append_htcuniversal ="\
	file://htcuni-acx.patch;patch=1;status=external \
	"

SRC_URI_append_zylonite ="\
	file://arm_pxa_20070923.patch;patch=1 \
	file://pxa_fb_overlay.patch;patch=1 \
	file://zylonite-boot.patch;patch=1 \
	"

S = "${WORKDIR}/linux-2.6.23"
