SECTION = "x11/network"
DESCRIPTION = "Mail user agent plugins"
DEPENDS = "claws-mail"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://www.claws-mail.org/downloads/plugins/mailmbox-${PV}.tar.gz"

inherit autotools pkgconfig

S = "${WORKDIR}/mailmbox-${PV}"

do_configure() {
    gnu-configize
    libtoolize --force
    oe_runconf
}

FILES_${PN} = "${libdir}/claws-mail/plugins/*.so"
FILES_${PN}-dbg = "${libdir}/claws-mail/plugins/.debug"
