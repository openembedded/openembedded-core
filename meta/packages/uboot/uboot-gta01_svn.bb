DESCRIPTION = "U-boot bootloader w/ Neo1973 (GTA01) support"
AUTHOR = "Harald Welte <laforge@openmoko.org>"
LICENSE = "GPL"
SECTION = "bootloader"
PRIORITY = "optional"
PV = "1.2.0+svn${SRCDATE}"
PR = "r3"
SRCDATE = "20070711"

PROVIDES = "virtual/bootloader"
S = "${WORKDIR}/git"

SRC_URI = "git://www.denx.de/git/u-boot.git/;protocol=git \
           svn://svn.openmoko.org/trunk/src/target/u-boot;module=patches;proto=http \
	   file://fix-arm920t-eabi.patch;patch=1"

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"
TARGET_LDFLAGS = ""
UBOOT_MACHINES = "gta01v3 gta01v4 gta01bv2 gta01bv3"

do_quilt() {
        mv ${WORKDIR}/patches ${S}/patches 
        cd ${S}
        quilt push -av
        rm -Rf patches .pc
}

do_compile () {
	chmod +x board/neo1973/split_by_variant.sh
	for type in ram nand
	do
		for mach in ${UBOOT_MACHINES}
		do
			oe_runmake ${mach}_config
			oe_runmake clean
			if [ ${type} == "ram" ]; then
				echo 'PLATFORM_RELFLAGS += -DBUILD_FOR_RAM' >> board/neo1973/config.tmp
			fi
			oe_runmake all
			mv u-boot.bin u-boot_${mach}_${type}.bin
		done
	done
}

do_deploy () {
	install -d ${DEPLOY_DIR_IMAGE}
	for type in nand ram
	do
		for mach in ${UBOOT_MACHINES}
		do
			install ${S}/u-boot_${mach}_${type}.bin ${DEPLOY_DIR_IMAGE}/u-boot_${type}-${mach}-${DATETIME}.bin
		done
	done
	install -m 0755 tools/mkimage ${STAGING_BINDIR_NATIVE}/uboot-mkimage
}

do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_compile
addtask quilt before do_patch after do_unpack
