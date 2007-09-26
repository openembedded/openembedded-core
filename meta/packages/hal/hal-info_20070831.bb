DESCRIPTION = "Hardware Abstraction Layer device information"
HOMEPAGE = "http://freedesktop.org/Software/hal"
SECTION = "unknown"
LICENSE = "GPL AFL"
DEPENDS = "hal"

SRC_URI = "git://anongit.freedesktop.org/hal-info/;protocol=git;tag=HAL_INFO_${PV}"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-recall --disable-video"

PACKAGE_ARCH = "all"
FILES_${PN} += "/usr/share/hal/"
