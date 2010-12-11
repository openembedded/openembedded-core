require mutter.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://download.moblin.org/sources/mutter/2.28/mutter-2.28.1_0.0.tar.bz2 \
           file://nodocs.patch;patch=1 \
           file://nozenity.patch;patch=1 \
           file://fix_pkgconfig.patch;patch=1 \
           "

S = "${WORKDIR}/mutter-2.28.1_0.0"
