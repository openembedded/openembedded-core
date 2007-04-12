DESCRIPTION = "Hardware Abstraction Layer device information"
HOMEPAGE = "http://freedesktop.org/Software/hal"
SECTION = "unknown"
LICENSE = "GPL AFL"

PV = "${SRCDATE}+git"
PR = "r1"


SRC_URI = "git://anongit.freedesktop.org/hal-info/;protocol=git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-recall --disable-video"


PACKAGE_ARCH = "all"
FILES_${PN} += "/usr/share/hal/"
