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

SRC_URI[md5sum] = "30ceb6df7e8681891e865e2fe5a7903d"
SRC_URI[sha256sum] = "e81fa140805b5cd029ff6dda5cfa94d223e83ac182ebcae94f841d62ce468829"

EXTRA_OECONF = "--disable-Werror --disable-device-mapper"

inherit autotools pkgconfig gettext

BBCLASSEXTEND = "native"
