require uboot-openmoko_svn.bb

PV = "1.2.0+git9912121f7ed804ea58fd62f3f230b5dcfc357d88svn2238"

SRC_URI = "git://www.denx.de/git/u-boot.git/;protocol=git;tag=9912121f7ed804ea58fd62f3f230b5dcfc357d88 \
file://uboot-machtypes.patch;patch=1 \
file://ext2load_hex.patch;patch=1 \
file://uboot-s3c2410-warnings-fix.patch;patch=1 \
file://uboot-strtoul.patch;patch=1 \
file://uboot-cramfs_but_no_jffs2.patch;patch=1 \
file://nand-read_write_oob.patch;patch=1 \
file://uboot-arm920t-gd_in_irq.patch;patch=1 \
file://uboot-arm920_s3c2410_irq_demux.patch;patch=1 \
file://uboot-s3c2410-nand.patch;patch=1 \
file://uboot-cmd_s3c2410.patch;patch=1 \
file://uboot-s3c2410-mmc.patch;patch=1 \
file://env_nand_oob.patch;patch=1 \
file://dynenv-harden.patch;patch=1 \
file://uboot-s3c2410_fb.patch;patch=1 \
file://uboot-20061030-qt2410.patch;patch=1 \
file://uboot-20061030-neo1973.patch;patch=1 \
file://uboot-s3c2410-misccr-definitions.patch;patch=1 \
file://boot-from-ram-reloc.patch;patch=1 \
file://boot-from-ram-and-nand.patch;patch=1 \
file://wakeup-reason-nand-only.patch;patch=1 \
file://uboot-neo1973-resume.patch;patch=1 \
file://nand-dynamic_partitions.patch;patch=1 \
file://uboot-s3c2410-norelocate_irqvec_cpy.patch;patch=1 \
file://uboot-usbtty-acm.patch;patch=1 \
file://uboot-s3c2410_udc.patch;patch=1 \
file://bbt-create-optional.patch;patch=1 \
file://nand-createbbt.patch;patch=1 \
file://dontask.patch;patch=1 \
file://nand-badisbad.patch;patch=1 \
file://uboot-bbt-quiet.patch;patch=1 \
file://raise-limits.patch;patch=1 \
file://splashimage-command.patch;patch=1 \
file://cmd-unzip.patch;patch=1 \
file://enable-splash-bmp.patch;patch=1 \
file://preboot-override.patch;patch=1 \
file://lowlevel_foo.patch;patch=1 \
file://default-env.patch;patch=1 \
file://console-ansi.patch;patch=1 \
file://boot-menu.patch;patch=1 \
file://uboot-dfu.patch;patch=1 \
file://uboot-neo1973-defaultenv.patch;patch=1 \
file://uboot-nand-markbad-reallybad.patch;patch=1 \
file://usbdcore-multiple_configs.patch;patch=1 \
file://neo1973-chargefast.patch;patch=1 \
file://uboot-s3c2440.patch;patch=1 \
file://uboot-smdk2440.patch;patch=1 \
file://uboot-hxd8.patch;patch=1 \
file://uboot-license.patch;patch=1 \
file://uboot-gta02.patch;patch=1 \
file://uboot-s3c2443.patch;patch=1 \
file://uboot-smdk2443.patch;patch=1 \
file://unbusy-i2c.patch;patch=1 \
file://makefile-no-dirafter.patch;patch=1 \
"

PROVIDES = ""
TARGET_LDFLAGS = ""

do_quilt() {
:
}

do_compile () {
        chmod +x board/neo1973/gta01/split_by_variant.sh
        oe_runmake gta01bv3_config
        oe_runmake clean
        oe_runmake tools
}

do_deploy () {
        install -m 0755 tools/mkimage ${STAGING_BINDIR_NATIVE}/uboot-mkimage
        ln -sf ${STAGING_BINDIR_NATIVE}/uboot-mkimage ${STAGING_BINDIR_NATIVE}/mkimage
}

do_deploy[dirs] = "${S}"
addtask deploy before do_package after do_install


