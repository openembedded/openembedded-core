require mutter.inc

PR = "r2"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "http://download.moblin.org/sources/mutter/2.28/mutter-2.28.1_0.0.tar.bz2 \
           file://nodocs.patch \
           file://nozenity.patch \
           file://fix_pkgconfig.patch \
           file://fix_CGL_TEXTURE_RECTANGLE_ARB.patch \
           "

SRC_URI[md5sum] = "39e8ee6ec701cd51b7121955d0ddb4fb"
SRC_URI[sha256sum] = "a56e2df7b53630cde99f82be1d72ca55caeb1760757979d04b656cc7b5420e76"

S = "${WORKDIR}/mutter-2.28.1_0.0"
