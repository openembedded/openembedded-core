require linux-nokia800.inc

PR = "r3"
SRC_URI = "http://repository.maemo.org/pool/maemo3.0/free/source/kernel-source-rx-34_2.6.18.orig.tar.gz \
           http://repository.maemo.org/pool/maemo3.0/free/source/kernel-source-rx-34_2.6.18-osso29.diff.gz;patch=1 \
           ${RPSRC}/lzo_kernel-r0.patch;patch=1 \
           ${RPSRC}/lzo_jffs2-r0.patch;patch=1 \
           ${RPSRC}/lzo_crypto-r0b.patch;patch=1 \
           ${RPSRC}/lzo_jffs2_lzomode-r0.patch;patch=1 \
           ${RPSRC}/lzo_jffs2_sysfs-r0.patch;patch=1 \
	   file://defconfig"

S = "${WORKDIR}/linux-g"

