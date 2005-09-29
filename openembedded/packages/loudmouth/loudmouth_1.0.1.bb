LICENSE = "LGPL"
DEPENDS = "glib-2.0 gnutls"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Loudmouth is a lightweight and easy-to-use C library for programming with the Jabber protocol."
PR = "r1"

SRC_URI = "http://ftp.imendio.com/pub/imendio/${PN}/src/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig

do_stage() {
	oe_libinstall -so -C loudmouth libloudmouth-1 ${STAGING_LIBDIR}

        install -d ${STAGING_INCDIR}/${PN}/
        install -m 0644 ${S}/${PN}/*.h ${STAGING_INCDIR}/${PN}/
}
