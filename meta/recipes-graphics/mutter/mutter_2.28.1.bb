require mutter.inc

PR = "r1"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://download.moblin.org/sources/mutter/2.28/mutter-2.28.1_0.0.tar.bz2 \
           file://nodocs.patch \
           file://nozenity.patch \
           file://fix_pkgconfig.patch \
           file://fix_CGL_TEXTURE_RECTANGLE_ARB.patch \
           "

S = "${WORKDIR}/mutter-2.28.1_0.0"
