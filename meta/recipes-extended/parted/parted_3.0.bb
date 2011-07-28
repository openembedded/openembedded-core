DESCRIPTION = "parted, the GNU partition resizing program"
HOMEPAGE = "http://www.gnu.org/software/parted/parted.html"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2f31b266d3440dd7ee50f92cf67d8e6c"
SECTION = "console/tools"
DEPENDS = "readline e2fsprogs"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/parted/parted-${PV}.tar.gz \
           file://no_check.patch \
           file://syscalls.patch "

SRC_URI[md5sum] = "a94e84a9b9944715c4453f82ccc639bf"
SRC_URI[sha256sum] = "8886eece015202f6bd5ce8414f4b68838452cef509f2e3389ad56128219837b7"

EXTRA_OECONF = "--disable-Werror --disable-device-mapper"

inherit autotools pkgconfig gettext

BBCLASSEXTEND = "native"
