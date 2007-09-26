DESCRIPTION = "Hardware Abstraction Layer device information"
HOMEPAGE = "http://freedesktop.org/Software/hal"
SECTION = "unknown"
LICENSE = "GPL AFL"
DEPENDS = "hal"

PV = "${SRCDATE}+git"
PR = "r2"


SRC_URI = "git://anongit.freedesktop.org/hal-info/;protocol=git;rev=HAL_INFO_20070831"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-recall --disable-video"


PACKAGE_ARCH = "all"
FILES_${PN} += "/usr/share/hal/"

# By default, use the released hal-info
DEFAULT_PREFERENCE = "-1"
