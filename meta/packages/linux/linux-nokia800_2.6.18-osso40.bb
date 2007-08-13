require linux-nokia800.inc

PR = "r4"
SRC_URI = "http://repository.maemo.org/pool/maemo3.1/free/source/kernel-source-rx-34_2.6.18.orig.tar.gz \
           http://repository.maemo.org/pool/maemo3.1/free/source/kernel-source-rx-34_2.6.18-osso40.diff.gz;patch=1 \
           ${RPSRC}/lzo_kernel-r0.patch;patch=1 \
           ${RPSRC}/lzo_jffs2-r0.patch;patch=1 \
           ${RPSRC}/lzo_crypto-r0b.patch;patch=1 \
           ${RPSRC}/lzo_jffs2_lzomode-r0.patch;patch=1 \
           ${RPSRC}/lzo_jffs2_sysfs-r0.patch;patch=1 \
           file://fix_oprofile.patch;patch=1 \
	   file://defconfig"

SRC_URI_append_nokia770 = " file://nokia770_nand_fix.patch;patch=1"

S = "${WORKDIR}/linux-g"

