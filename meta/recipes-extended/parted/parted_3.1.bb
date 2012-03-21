DESCRIPTION = "parted, the GNU partition resizing program"
HOMEPAGE = "http://www.gnu.org/software/parted/parted.html"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=2f31b266d3440dd7ee50f92cf67d8e6c"
SECTION = "console/tools"
DEPENDS = "ncurses readline util-linux"
PR = "r0"

SRC_URI = "${GNU_MIRROR}/parted/parted-${PV}.tar.xz \
           file://no_check.patch \
           file://syscalls.patch \
           file://fix-git-version-gen.patch \
           file://fix-doc-mandir.patch"

SRC_URI[md5sum] = "5d89d64d94bcfefa9ce8f59f4b81bdcb"
SRC_URI[sha256sum] = "5e9cc1f91eaf016e5033d85b9b893fd6d3ffaca532a48de1082df9b94225ca15"

EXTRA_OECONF = "--disable-device-mapper"

inherit autotools pkgconfig gettext

BBCLASSEXTEND = "native"
