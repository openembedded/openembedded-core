PV = "${SRCDATE}+git"

DESCRIPTION = "Hardware Abstraction Layer device information"
HOMEPAGE = "http://freedesktop.org/Software/hal"
SECTION = "unknown"
LICENSE = "GPL AFL"

SRC_URI = "git://anongit.freedesktop.org/hal-info/;protocol=git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-recall --disable-video"

FILES_${PN} += "/usr/share/hal/"
