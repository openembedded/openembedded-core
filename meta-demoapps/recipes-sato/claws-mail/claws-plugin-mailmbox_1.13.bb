SECTION = "x11/network"
DESCRIPTION = "Mail user agent plugins"
DEPENDS = "claws-mail"
LICENSE = "GPL"
PR = "r2"

SRC_URI = "http://www.claws-mail.org/downloads/plugins/mailmbox-${PV}.tar.gz"

inherit autotools pkgconfig

S = "${WORKDIR}/mailmbox-${PV}"

FILES_${PN} = "${libdir}/claws-mail/plugins/*.so"
FILES_${PN}-dbg = "${libdir}/claws-mail/plugins/.debug"
