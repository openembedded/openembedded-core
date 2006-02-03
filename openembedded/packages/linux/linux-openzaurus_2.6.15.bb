include linux-openzaurus.inc

PR = "r2"

# Handy URLs
# git://rsync.kernel.org/pub/scm/linux/kernel/git/torvalds/linux-2.6.git \
# http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.14.tar.gz \
# http://www.kernel.org/pub/linux/kernel/v2.6/testing/patch-2.6.15-rc1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/v2.6/snapshots/patch-2.6.15-rc2-git1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/alan/linux-2.6/2.6.10/patch-2.6.10-ac8.gz;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/akpm/patches/2.6/2.6.14-rc2/2.6.14-rc2-mm1/2.6.14-rc2-mm1.bz2;patch=1 \	   

# Patches submitted upstream are towards top of this list 
# Hacks should clearly named and at the bottom
SRC_URI = "http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.15.tar.bz2 \
           ${RPSRC}/pxa_ohci_platform-r5.patch;patch=1 \
           ${RPSRC}/pxa_ohci_suspend-r5.patch;patch=1 \
           ${RPSRC}/pxa_ohci_fixes-r0.patch;patch=1 \
           ${RPSRC}/spitz_ohci-r0.patch;patch=1 \
           ${RPSRC}/pxa_i2c_fixes-r5.patch;patch=1 \
           ${RPSRC}/scoop_cpr_fix-r1.patch;patch=1 \
           ${RPSRC}/pxa2xx_release-r1.patch;patch=1 \
           ${RPSRC}/arm_apm_pm_legacy-r1.patch;patch=1 \
           ${RPSRC}/ide_not_removable-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_pm_move-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_pm_gcc4_fix-r0.patch;patch=1 \
           ${RPSRC}/pcmcia_dev_ids-r3.patch;patch=1 \
           ${RPSRC}/led_core-r11.patch;patch=1 \
           ${RPSRC}/led_triggers-r9.patch;patch=1 \
           ${RPSRC}/led_trig_timer-r4.patch;patch=1 \
           ${RPSRC}/led_trig_sharpsl_pm-r4a.patch;patch=1 \
           ${RPSRC}/led_zaurus-r8.patch;patch=1 \
           ${RPSRC}/led_locomo-r5.patch;patch=1 \
           ${RPSRC}/led_ixp4xx-r0.patch;patch=1 \
           ${RPSRC}/led_tosa-r2.patch;patch=1 \	   
           ${RPSRC}/led_ide-r2.patch;patch=1 \
           ${RPSRC}/led_nand-r2.patch;patch=1 \
           ${RPSRC}/pxa_timerfix-r0.patch;patch=1 \
           ${RPSRC}/pxa_rtc-r3.patch;patch=1 \
           ${RPSRC}/input_power-r4.patch;patch=1 \
           ${RPSRC}/jffs2_longfilename-r0.patch;patch=1 \
           ${RPSRC}/pxa25x_cpufreq-r0.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_base-r24.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_bl-r4.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_pcmcia-r2.patch;patch=1 \
           ${RPSRC}/ipaq/pxa_keys-r5.patch;patch=1 \
           ${RPSRC}/ipaq/tsc2101-r12.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_test1-r3.patch;patch=1 \
           ${DOSRC}/tc6393-device-r8.patch;patch=1 \
           ${DOSRC}/tc6393_nand-r7.patch;patch=1 \
           ${DOSRC}/tosa-keyboard-r9.patch;patch=1 \
           ${RPSRC}/temp/tosa-pxaac97-r5-rp.patch;patch=1 \
           ${RPSRC}/temp/tosa-tc6393-r1-rp.patch;patch=1 \
           ${RPSRC}/temp/tosa-power-r9-rp.patch;patch=1 \
           ${DOSRC}/tc6393fb-r9.patch;patch=1 \
           ${RPSRC}/temp/tosa-lcd-r6-rp.patch;patch=1 \
           ${DOSRC}/tosa-bl-r9.patch;patch=1 \
           ${DOSRC}/tosa-bluetooth-r2.patch;patch=1 \
           ${RPSRC}/mmc_timeout-r0.patch;patch=1 \	   
           ${RPSRC}/pxa_cf_initorder_hack-r1.patch;patch=1 \
           ${RPSRC}/usb_pxa27x_udc-r0.patch;patch=1 \
           ${RPSRC}/usb_add_epalloc-r1.patch;patch=1 \
           ${RPSRC}/alsa/alsa-soc-0.9.patch;patch=1 \
           ${RPSRC}/alsa/alsa-soc-0.9-0.10rc1.patch;patch=1 \
           ${RPSRC}/alsa_soc_rpupdate7-r2.patch;patch=1 \
           ${RPSRC}/alsa_akita_fix-r0.patch;patch=1 \
           ${RPSRC}/zaurus_keyboard_tweak-r0.patch;patch=1 \
           ${RPSRC}/corgi_bl_cleanup-r2.patch;patch=1 \
           ${RPSRC}/corgi_bl_generic-r2.patch;patch=1 \
           ${RPSRC}/arm_checksum_memory-r0.patch;patch=1 \
           ${RPSRC}/pxafb_tweaks-r0.patch;patch=1 \
           ${RPSRC}/pxa_clocks-r0.patch;patch=1 \
           ${RPSRC}/pm_changes-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_pm-do-r1.patch;patch=1 \
           file://add-oz-release-string.patch;patch=1 \
           file://pxa-serial-hack.patch;patch=1 \
           ${RPSRC}/jl1/pxa-linking-bug.patch;patch=1 \
           file://serial-add-support-for-non-standard-xtals-to-16c950-driver.patch;patch=1 \
           file://connectplus-remove-ide-HACK.patch;patch=1 \
           file://defconfig-c7x0 \
           file://defconfig-ipaq-pxa270 \
           file://defconfig-collie \
           file://defconfig-poodle \
           file://defconfig-cxx00 \
           file://defconfig-tosa "

# Disabled until I find the reason this gives issues with cdc_subset
#            ${RPSRC}/usb_rndis_tweaks-r0.patch;patch=1 \

# These patches would really help collie/poodle but we 
# need someone to maintain them
# ${JLSRC}/zaurus-lcd-2.6.11.diff.gz;patch=1 
#   (Pavel Machek's git tree has updated versions of this?)
#   Also parts were recently committed to mainline by rmk (drivers/mfd/)
# ${JLSRC}/zaurus-base-2.6.11.diff.gz;patch=1 
#   (This is mostly in mainline now?)
# ${JLSRC}/zaurus-local-2.6.11.diff.gz;patch=1 \
# ${JLSRC}/zaurus-leds-2.6.11.diff.gz;patch=1 \

SRC_URI_append_tosa = "${DOSRC}/nand-readid-r1.patch;patch=1 \
		       ${DOSRC}/ac97codec-rename-revert-r0.patch;patch=1 \
                       ${DOSRC}/wm97xx-touch-lg2-r0.patch;patch=1 \
                       ${DOSRC}/wm9712-pm-r0.patch;patch=1 "

S = "${WORKDIR}/linux-2.6.15"

# to get module dependencies working
KERNEL_RELEASE = "2.6.15"
