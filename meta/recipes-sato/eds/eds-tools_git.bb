LICENSE = "GPLv2"
SECTION = "x11"
DEPENDS = "dbus-glib eds-dbus"
RDEPENDS_${PN} = "libedata-book"
DESCRIPTION = "Test applications for EDS"

SRCREV = "882df681014cf42f75882995e507c75254b6b62f"
PR = "r0"

SRC_URI = "git://github.com/rossburton/eds-tools.git;protocol=git"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

inherit autotools pkgconfig

S = "${WORKDIR}/${BPN}"

FILES_${PN} += "${libdir}/evolution-data-server-1.2/extensions/*.so"
