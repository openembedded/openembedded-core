SECTION = "x11/network"
DESCRIPTION = "Mail user agent plugins"
DEPENDS = "claws-mail libxml2 curl glib-2.0 gtk+"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://www.claws-mail.org/downloads/plugins/rssyl-${PV}.tar.gz"

inherit autotools pkgconfig

S = "${WORKDIR}/rssyl-${PV}"

do_configure() {
    gnu-configize
    libtoolize --force
    oe_runconf
}

FILES_${PN} = "${libdir}/claws-mail/plugins/*.so"

