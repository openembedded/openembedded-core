DESCRIPTION = "Hardware Abstraction Layer device information"
HOMEPAGE = "http://hal.freedesktop.org/"
BUGTRACKER = "http://bugs.freedesktop.org/buglist.cgi?product=hal"
SECTION = "unknown"

LICENSE = "GPLv2 | AFL"
LIC_FILES_CHKSUM = "file://COPYING;md5=ae484909bad0cf11c8e2071cee6bd4dc"

SRC_URI = "http://hal.freedesktop.org/releases/${BPN}-${PV}.tar.gz"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-recall --disable-video"

do_configure() {
        gnu-configize
	libtoolize --force
	oe_runconf
}

FILES_${PN} += "${datadir}/hal/"
