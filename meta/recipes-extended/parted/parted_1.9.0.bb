DESCRIPTION = "parted, the GNU partition resizing program"
HOMEPAGE = "http://www.gnu.org/software/parted/parted.html"
LICENSE = "GPLv2"
SECTION = "console/tools"
DEPENDS = "readline e2fsprogs"
PR = "r3"

SRC_URI = "${GNU_MIRROR}/parted/parted-${PV}.tar.gz \
           file://no_check.patch;patch=1 \
           file://syscalls.patch;patch=1 "

EXTRA_OECONF = "--disable-Werror --disable-device-mapper"

inherit autotools pkgconfig gettext

BBCLASSEXTEND = "native"
