include linux-openzaurus.inc

PR = "r2"

# Handy URLs
# http://www.kernel.org/pub/linux/kernel/people/alan/linux-2.6/2.6.10/patch-2.6.10-ac8.gz;patch=1 \
# http://www.kernel.org/pub/linux/kernel/v2.6/snapshots/patch-2.6.12-rc4-git1.bz2;patch=1 \
# ftp://ftp.kernel.org/pub/linux/kernel/v2.6/testing/patch-2.6.14-rc1.bz2;patch=1 \
# git://rsync.kernel.org/pub/scm/linux/kernel/git/torvalds/linux-2.6.git \

# Patches submitted upstream are towards top of this list 
# Hacks should clearly named and at the bottom
SRC_URI = "ftp://ftp.kernel.org/pub/linux/kernel/v2.6/linux-2.6.13.tar.gz \
           ftp://ftp.kernel.org/pub/linux/kernel/v2.6/testing/patch-2.6.14-rc1.bz2;patch=1 \
           ${RPSRC}/borzoi_machtype-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_ssp-r4.patch;patch=1 \
           ${RPSRC}/sharpsl_lcd-r4.patch;patch=1 \
           ${RPSRC}/sharpsl_ts-r6.patch;patch=1 \
           ${RPSRC}/sharpsl_bl-r5a.patch;patch=1 \
           ${RPSRC}/spitzkbd-r3.patch;patch=1 \
           ${RPSRC}/spitz_base-r17.patch;patch=1 \
           ${RPSRC}/pxa_i2c_fixes-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_mtd_extras-r1.patch;patch=1 \
           ${RPSRC}/pxa_ohci_platform-r1.patch;patch=1 \
           ${RPSRC}/pxa_ohci_suspend-r0.patch;patch=1 \	 
           ${RPSRC}/corgi_cleanup-r0.patch;patch=1 \
           ${RPSRC}/poodle_cleanup-r0.patch;patch=1 \
           ${RPSRC}/poodle_mmc-r0.patch;patch=1 \
           ${RPSRC}/poodle_irda-r0.patch;patch=1 \
           ${RPSRC}/ide_not_removable-r0.patch;patch=1 \
           ${RPSRC}/scoop_collie_fix-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_pm-r8.patch;patch=1 \
           ${RPSRC}/corgi_pm-r3.patch;patch=1 \
           ${RPSRC}/spitz_base_extras-r2.patch;patch=1 \
           ${RPSRC}/spitz_pm-r4.patch;patch=1 \
           ${RPSRC}/spitz_kbd_fix1-r0.patch;patch=1 \
           ${RPSRC}/spitzcf-r3.patch;patch=1 \
           ${RPSRC}/pxa_timerfix-r0.patch;patch=1 \
           ${RPSRC}/pxa_remove_static-r0.patch;patch=1 \
           ${RPSRC}/pxa_irda-r4.patch;patch=1 \
           ${RPSRC}/corgi_irda-r3.patch;patch=1 \
           ${RPSRC}/pxa_rtc-r1.patch;patch=1 \
           ${RPSRC}/scoop_susres-r1.patch;patch=1 \
           ${RPSRC}/input_power-r2.patch;patch=1 \
           ${RPSRC}/jffs2_longfilename-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_bl_kick-r1.patch;patch=1 \
           ${RPSRC}/corgi_snd-r10.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_base-r20.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_bl-r2.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_pcmcia-r1.patch;patch=1 \
           ${RPSRC}/ipaq/pxa_keys-r2.patch;patch=1 \
           ${RPSRC}/ipaq/tsc2101-r8.patch;patch=1 \
           ${RPSRC}/ipaq/hx2750_test1-r2.patch;patch=1 \
           ${DOSRC}/pxa2xx-ir-dma-r0.patch;patch=1 \
           ${DOSRC}/tc6393-device-r5.patch;patch=1 \
           ${DOSRC}/tc6393_nand-r6.patch;patch=1 \
           ${DOSRC}/tosa-machine-base-r9.patch;patch=1 \
           ${DOSRC}/tosa-keyboard-r6.patch;patch=1 \
           ${RPSRC}/temp/tc6393fb-r7.patch;patch=1 \
           ${DOSRC}/tosa-power-r6.patch;patch=1 \
           ${DOSRC}/tosa-mmc-r5.patch;patch=1 \
           ${DOSRC}/tosa-udc-r4.patch;patch=1 \
           ${DOSRC}/tosa-irda-r3.patch;patch=1 \
           ${DOSRC}/tosa-lcd-r3.patch;patch=1 \
           ${RPSRC}/temp/tosa-bl-r5a.patch;patch=1 \
           ${RPSRC}/pcmcia_dev_ids-r2.patch;patch=1 \
           ${RPSRC}/mmc_timeout-r0.patch;patch=1 \	   
           ${RPSRC}/pxa_cf_initorder_hack-r1.patch;patch=1 \
           file://add-oz-release-string.patch;patch=1 \
           file://add-elpp-stuff.patch;patch=1 \
           file://pxa-serial-hack.patch;patch=1 \
           ${RPSRC}/jl1/pxa-linking-bug.patch;patch=1 \
           file://dtl1_cs-add-socket-revE.patch;patch=1 \
           file://connectplus-remove-ide-HACK.patch;patch=1 \
           file://defconfig-c7x0 \
           file://defconfig-ipaq-pxa270 \
           file://defconfig-collie \
           file://defconfig-poodle \
           file://defconfig-cxx00 \
           file://defconfig-tosa "

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
                       ${DOSRC}/pxa-ac97-suspend-r0.patch;patch=1 \
		       ${DOSRC}/ac97codec-rename-revert-r0.patch;patch=1 \
                       ${DOSRC}/wm9712-ts-r3.patch;patch=1 \
                       ${DOSRC}/tosa-pxaac97-r4.patch;patch=1 \
        	       ${DOSRC}/tosa-bluetooth-r0.patch;patch=1 "

S = "${WORKDIR}/linux-2.6.13"
