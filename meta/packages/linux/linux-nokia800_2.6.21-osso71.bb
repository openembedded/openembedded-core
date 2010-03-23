require linux-nokia800.inc
PR = "r4"

DEFAULT_PREFERENCE_nokia770 = "-1"

SRC_URI = "${KERNELORG_MIRROR}/pub/linux/kernel/v2.6/linux-2.6.21.tar.bz2 \
	   http://repository.maemo.org/pool/os2008/free/source/k/kernel-source-rx-34/kernel-source-rx-34_2.6.21.0-osso71.diff.gz;patch=1 \
           http://www.rpsys.net/openzaurus/patches/archive/input_power-r7.patch;patch=1 \
	   file://suspend-button.patch;patch=1 \
	   file://defconfig"

S = "${WORKDIR}/linux-2.6.21"

do_install_append () {
	mkdir -p ${D}/kernel/include/drivers/media/video/omap/
	cp -f drivers/media/video/omap/tcm825x.h ${D}/kernel/include/drivers/media/video/omap/
}
