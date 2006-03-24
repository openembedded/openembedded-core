include linux-openzaurus.inc

PR = "r2"

DEFAULT_PREFERENCE = "-1"

# Handy URLs
# git://rsync.kernel.org/pub/scm/linux/kernel/git/torvalds/linux-2.6.git \
# http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.14.tar.gz \
# http://www.kernel.org/pub/linux/kernel/v2.6/testing/patch-2.6.15-rc1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/v2.6/snapshots/patch-2.6.15-rc2-git1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/alan/linux-2.6/2.6.10/patch-2.6.10-ac8.gz;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/akpm/patches/2.6/2.6.14-rc2/2.6.14-rc2-mm1/2.6.14-rc2-mm1.bz2;patch=1 \	   

# Patches submitted upstream are towards top of this list 
# Hacks should clearly named and at the bottom
SRC_URI = "http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.16.tar.bz2 \
           ${RPSRC}/led_core-r14.patch;patch=1 \
           ${RPSRC}/led_triggers-r11.patch;patch=1 \
           ${RPSRC}/led_trig_timer-r6.patch;patch=1 \
           ${RPSRC}/led_trig_sharpsl_pm-r5.patch;patch=1 \
           ${RPSRC}/led_zaurus-r10.patch;patch=1 \
           ${RPSRC}/led_locomo-r7.patch;patch=1 \
           ${RPSRC}/led_ixp4xx-r2.patch;patch=1 \
           ${RPSRC}/led_tosa-r5.patch;patch=1 \
           ${RPSRC}/led_ide-r4a.patch;patch=1 \
           ${RPSRC}/led_nand-r3.patch;patch=1 \
           ${RPSRC}/ide_end_request-r1.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-library-functions.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-arm-cleanup-r1.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-class.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-class-fix.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-class-fix-2.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-i2c-cleanup-r1.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-sysfs-interface.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-proc-interface.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-dev-interface.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-x1205-driver.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-test-device-driver.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-ds1672-driver.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-pcf8563-driver.patch;patch=1 \
           ${RPSRC}/rtc-subsystem-rs5c372-driver.patch;patch=1 \
           ${RPSRC}/rtc_class_pxa-r2.patch;patch=1 \
           ${RPSRC}/rmk_pxa_mmc_timeout-r0.patch;patch=1 \
           ${RPSRC}/integrator_rtc-r0.patch;patch=1 \
           ${RPSRC}/zaurus_keyboard_tweak-r3.patch;patch=1 \
           ${RPSRC}/pxafb_tweaks-r0.patch;patch=1 \
           ${RPSRC}/alsa/asoc-v0.10rc4.patch;patch=1 \
           ${RPSRC}/hx2750_base-r24.patch;patch=1 \
           ${RPSRC}/hx2750_bl-r5.patch;patch=1 \
           ${RPSRC}/hx2750_pcmcia-r2.patch;patch=1 \
           ${RPSRC}/pxa_keys-r5.patch;patch=1 \
           ${RPSRC}/tsc2101-r12.patch;patch=1 \
           ${RPSRC}/hx2750_test1-r3.patch;patch=1 \
           ${RPSRC}/pxa_timerfix-r0.patch;patch=1 \
           ${RPSRC}/input_power-r4.patch;patch=1 \
           ${RPSRC}/jffs2_longfilename-r0.patch;patch=1 \
           ${RPSRC}/pxa25x_cpufreq-r0.patch;patch=1 \
           ${RPSRC}/misc_fix1-r0.patch;patch=1 \
           ${RPSRC}/corgi_bl_cleanup-r2.patch;patch=1 \
           ${RPSRC}/corgi_bl_generic-r2.patch;patch=1 \
           ${RPSRC}/pm_changes-r1.patch;patch=1 \
           ${RPSRC}/sharpsl_pm-do-r2.patch;patch=1 \
           ${RPSRC}/usb_pxa27x_udc-r0.patch;patch=1 \
           ${RPSRC}/usb_add_epalloc-r1.patch;patch=1 \
	   ${DOSRC}/kexec-arm-r2.patch;patch=1 \
           ${RPSRC}/pxa_cf_initorder_hack-r1.patch;patch=1 \
           ${RPSRC}/mmcsd_no_scr_check-r0.patch;patch=1 \
           file://add-oz-release-string.patch;patch=1 \
           file://pxa-serial-hack.patch;patch=1 \
           ${RPSRC}/pxa-linking-bug.patch;patch=1 \
           file://serial-add-support-for-non-standard-xtals-to-16c950-driver.patch;patch=1 \
           file://connectplus-remove-ide-HACK.patch;patch=1 \
           file://defconfig-c7x0 \
           file://defconfig-ipaq-pxa270 \
           file://defconfig-collie \
           file://defconfig-poodle \
           file://defconfig-cxx00 \
           file://defconfig-qemuarm \
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

SRC_URI_append_tosa = "\
	   ${CHSRC}/usb-ohci-hooks-r1.patch;patch=1 \
	   ${CHSRC}/tmio-core-r4.patch;patch=1 \
	   ${CHSRC}/tmio-tc6393-r5.patch;patch=1 \
	   ${CHSRC}/tmio-nand-r5.patch;patch=1 \
	   ${CHSRC}/tmio-ohci-r3.patch;patch=1 \
	   ${CHSRC}/tmio-fb-r6.patch;patch=1 \
	   ${DOSRC}/tosa-keyboard-r13.patch;patch=1 \
	   ${DOSRC}/tosa-pxaac97-r6.patch;patch=1 \
	   ${DOSRC}/tosa-tmio-r4.patch;patch=1 \
	   ${DOSRC}/tosa-power-r14.patch;patch=1 \
	   ${DOSRC}/tosa-tmio-lcd-r5.patch;patch=1 \
	   ${DOSRC}/tosa-bluetooth-r5.patch;patch=1 \
	   ${DOSRC}/wm97xx-lg7-r0.patch;patch=1 \
	   ${DOSRC}/tosa-asoc-r0.patch;patch=1 "

S = "${WORKDIR}/linux-2.6.16"

# to get module dependencies working
KERNEL_RELEASE = "2.6.16"
