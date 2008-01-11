SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "zlib"
DESCRIPTION = "Library for interacting with ID3 tags."
LICENSE = "GPL"
PR = "r1"

SRC_URI = "ftp://ftp.mars.org/pub/mpeg/libid3tag-${PV}.tar.gz \
           file://id3tag.pc"

S = "${WORKDIR}/libid3tag-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "-enable-speed"

do_configure_prepend() {
    install -m 0644 ${WORKDIR}/id3tag.pc ${S}
}
do_stage() {
	oe_libinstall -so libid3tag ${STAGING_LIBDIR}
        install -m 0644 id3tag.h ${STAGING_INCDIR}
}
