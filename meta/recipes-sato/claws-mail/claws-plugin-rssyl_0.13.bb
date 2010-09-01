SECTION = "x11/network"
DESCRIPTION = "Mail user agent plugins"
DEPENDS = "claws-mail libxml2 curl glib-2.0 gtk+"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://www.claws-mail.org/downloads/plugins/rssyl-${PV}.tar.gz"

inherit autotools pkgconfig gettext

S = "${WORKDIR}/rssyl-${PV}"

FILES_${PN} = "${libdir}/claws-mail/plugins/*.so"

