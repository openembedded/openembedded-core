DESCRIPTION = "Hardware Abstraction Layer device information"
HOMEPAGE = "http://hal.freedesktop.org/"
BUGTRACKER = "http://bugs.freedesktop.org/buglist.cgi?product=hal"
SECTION = "unknown"

LICENSE = "GPLv2 | AFL"

SRC_URI = "http://hal.freedesktop.org/releases/${PN}-${PV}.tar.gz"
S = "${WORKDIR}/${PN}-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-recall --disable-video"

do_configure() {
        gnu-configize
	libtoolize --force
	oe_runconf
}

PACKAGE_ARCH = "all"
FILES_${PN} += "${datadir}/hal/"
