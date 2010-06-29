DESCRIPTION = "Speex is an Open Source/Free Software patent-free audio compression format designed for speech."
HOMEPAGE = "http://www.speex.org"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=314649d8ba9dd7045dfb6683f298d0a8 \
                    file://include/speex/speex.h;beginline=1;endline=34;md5=a68129f78d7fe66e07163f73aba143b3"
DEPENDS = "libogg"

PR = "r0"

SRC_URI = "http://downloads.us.xiph.org/releases/speex/speex-1.2rc1.tar.gz"

PARALLEL_MAKE = ""

inherit autotools pkgconfig

EXTRA_OECONF = " --enable-fixed-point --with-ogg-libraries=${STAGING_LIBDIR} \
                 --disable-float-api --disable-vbr \
                 --with-ogg-includes=${STAGING_INCDIR} --disable-oggtest"

PACKAGES += "${PN}-bin"
FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${libdir}/lib*.so.*"
FILES_${PN}-bin = "${bindir}"
