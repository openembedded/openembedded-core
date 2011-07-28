LICENSE = "GPLv2"
SECTION = "x11"
DEPENDS = "dbus-glib eds-dbus"
RDEPENDS_${PN} = "libedata-book"
DESCRIPTION = "Test applications for EDS"

SRCREV = "2008-02-04"
PR = "r1"

SRC_URI = "bzr://burtonini.com/bzr/eds-tools;proto=http"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

inherit autotools pkgconfig

S = "${WORKDIR}/${BPN}"

FILES_${PN} += "${libdir}/evolution-data-server-1.2/extensions/*.so"
