LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "dbus-glib eds-dbus"
RDEPENDS = "libedata-book"
DESCRIPTION = "Test applications for EDS"

SRC_URI = "bzr://burtonini.com/bzr/eds-tests;proto=http"

inherit autotools pkgconfig

S = "${WORKDIR}/${PN}"

FILES_${PN} += "${libdir}/evolution-data-server-1.2/extensions/*.so"
