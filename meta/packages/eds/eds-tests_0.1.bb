LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "dbus-glib eds-dbus"
RDEPENDS = "libedata-book"
DESCRIPTION = "Test applications for EDS"

PR = "r1"

SRC_URI = "http://burtonini.com/temp/${PN}-${PV}.tar.gz"

inherit autotools pkgconfig

S = "${WORKDIR}/${PN}-${PV}"

FILES_${PN} += "${libdir}/evolution-data-server-1.2/extensions/*.so"
