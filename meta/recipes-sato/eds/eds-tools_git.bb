LICENSE = "GPLv2"
SECTION = "x11"
DEPENDS = "dbus-glib eds-dbus"
RDEPENDS_${PN} = "libedata-book"
DESCRIPTION = "Test applications for EDS"

SRCREV = "5e9afbd22a021d1f0d0a0249d5995d19dd770584"
PR = "r1"

SRC_URI = "git://github.com/rossburton/eds-tools.git;protocol=git"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

inherit autotools pkgconfig

S = "${WORKDIR}/git"

FILES_${PN} += "${libdir}/evolution-data-server-1.2/extensions/*.so"
FILES_${PN} += "${libdir}/evolution-data-server-1.2/extensions/*.la"
FILES_${PN}-dbg += "${libdir}/evolution-data-server-1.2/extensions/.debug/*.so"
