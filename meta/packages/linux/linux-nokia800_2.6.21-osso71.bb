require linux-nokia800.inc

DEFAULT_PREFERENCE = "-1"
DEFAULT_PREFERENCE_nokia800 = "1"

SRC_URI = "${KERNELORG_MIRROR}pub/linux/kernel/v2.6/linux-2.6.21.tar.bz2 \
	   http://repository.maemo.org/pool/os2008/free/source/k/kernel-source-rx-34/kernel-source-rx-34_2.6.21.0-osso71.diff.gz;patch=1 \
	   file://defconfig"

S = "${WORKDIR}/linux-2.6.21"
