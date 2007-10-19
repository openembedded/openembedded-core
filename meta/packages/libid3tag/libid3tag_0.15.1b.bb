SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "zlib"
DESCRIPTION = "Library for interacting with ID3 tags."
LICENSE = "GPL"

SRC_URI = "${SOURCEFORGE_MIRROR}/mad/libid3tag-${PV}.tar.gz"
S = "${WORKDIR}/libid3tag-${PV}"

inherit autotools

EXTRA_OECONF = "-enable-speed"

do_stage() {
	oe_libinstall -so libid3tag ${STAGING_LIBDIR}
        install -m 0644 id3tag.h ${STAGING_INCDIR}
}
