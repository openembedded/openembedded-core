SECTION = "x11/network"
DESCRIPTION = "Mail user agent plugins"
DEPENDS = "claws-mail gtkhtml2 curl"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://www.claws-mail.org/downloads/plugins/gtkhtml2_viewer-${PV}.tar.gz"

inherit autotools pkgconfig gettext

S = "${WORKDIR}/gtkhtml2_viewer-${PV}"

FILES_${PN} = "${libdir}/claws-mail/plugins/*.so"

