require nokia800.inc

PR = "r2"
SRC_URI = "http://repository.maemo.org/pool/maemo3.0/free/source/kernel-source-rx-34_2.6.18.orig.tar.gz \
           http://repository.maemo.org/pool/maemo3.0/free/source/kernel-source-rx-34_2.6.18-osso29.diff.gz;patch=1 \
	   file://defconfig"

S = "${WORKDIR}/linux-g"

