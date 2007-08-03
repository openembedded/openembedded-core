DESCRIPTION = "Speex is an Open Source/Free Software patent-free audio compression format designed for speech."
SECTION = "libs"
LICENSE = "BSD"
HOMEPAGE = "http://www.speex.org"
DEPENDS = "libogg"

SRC_URI = "http://downloads.us.xiph.org/releases/speex/speex-1.2beta2.tar.gz"
S = "${WORKDIR}/${PN}-1.2beta2"

PARALLEL_MAKE = ""

inherit autotools pkgconfig

EXTRA_OECONF = " --enable-fixed-point --with-ogg-libraries=${STAGING_LIBDIR} \
                 --with-ogg-includes=${STAGING_INCDIR} --disable-oggtest"

do_stage() {
    autotools_stage_all
}

PACKAGES += "${PN}-bin"
FILES_${PN} = "${libdir}/libspeex.so.*"
FILES_${PN}-dev += "${libdir}/libspeex.so.*"
FILES_${PN}-bin = "${bindir}"
