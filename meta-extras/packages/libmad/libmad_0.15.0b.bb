DESCRIPTION = "MPEG Audio Decoder Library"
SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "libid3tag"
LICENSE = "GPL"
PR = "r2"

SRC_URI = "ftp://ftp.mars.org/pub/mpeg/libmad-${PV}.tar.gz"
S = "${WORKDIR}/libmad-${PV}"

inherit autotools 

EXTRA_OECONF = "-enable-speed --enable-shared"
# The ASO's don't take any account of thumb...
EXTRA_OECONF_append_thumb = " --disable-aso --enable-fpm=default"

do_configure_prepend () {
#	damn picky automake...
	touch NEWS AUTHORS ChangeLog
}

do_stage() {
	oe_libinstall -so libmad ${STAGING_LIBDIR}
	install -m 0644 mad.h ${STAGING_INCDIR}
}
