DESCRIPTION = "parted, the GNU partition resizing program"
HOMEPAGE = "http://www.gnu.org/software/parted/parted.html"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4c61b8950dc1aab4d2aa7c2ae6b1cfb3"
SECTION = "console/tools"
DEPENDS = "readline e2fsprogs"
PR = "r3"

SRC_URI = "${GNU_MIRROR}/parted/parted-${PV}.tar.gz \
           file://no_check.patch;patch=1 \
           file://syscalls.patch;patch=1 "

SRC_URI[md5sum] = "055305bc7bcf472ce38f9abf69a9d94d"
SRC_URI[sha256sum] = "53231241ba24c104c207bd2920d70d31fda86df86a69c90e11f35144ffc55509"

EXTRA_OECONF = "--disable-Werror --disable-device-mapper"

inherit autotools pkgconfig gettext

BBCLASSEXTEND = "native"
