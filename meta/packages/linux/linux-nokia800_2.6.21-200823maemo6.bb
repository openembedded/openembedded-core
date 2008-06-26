require linux-nokia800.inc
PR = "r0"

DEFAULT_PREFERENCE_nokia770 = "-1"

SRC_URI = "${KERNELORG_MIRROR}pub/linux/kernel/v2.6/linux-2.6.21.tar.bz2 \
	   file://kernel-source_2.6.21-200823maemo6.diff.gz;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/input_power-r7.patch;patch=1 \
	   file://suspend-button.patch;patch=1 \
	   file://defconfig"

S = "${WORKDIR}/linux-2.6.21"

do_stage_append () {
	mkdir -p ${STAGING_KERNEL_DIR}/drivers/media/video/omap/
	cp -f drivers/media/video/omap/tcm825x.h ${STAGING_KERNEL_DIR}/drivers/media/video/omap/
}
