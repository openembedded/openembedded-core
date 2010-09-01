SECTION = "x11/network"
DESCRIPTION = "Mail user agent plugins"
DEPENDS = "claws-mail db"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://www.claws-mail.org/downloads/plugins/maildir-${PV}.tar.gz"

inherit autotools pkgconfig

S = "${WORKDIR}/maildir-${PV}"

FILES_${PN} = "${libdir}/claws-mail/plugins/*.so"

