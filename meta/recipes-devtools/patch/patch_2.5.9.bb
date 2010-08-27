require patch.inc

SRC_URI = "${GNU_MIRROR}/patch/patch-2.5.4.tar.gz \
	   file://2.5.9.patch;patch=1 \
	   file://debian.patch;patch=1 \
	   file://install.patch;patch=1 \
           file://unified-reject-files.diff;patch=1 \
           file://global-reject-file.diff;patch=1 "
S = "${WORKDIR}/patch-2.5.4"
PR = "r2"