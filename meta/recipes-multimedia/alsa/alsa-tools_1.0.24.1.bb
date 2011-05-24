BROKEN = "1"

DESCRIPTION = "Alsa Tools"
SECTION = "console/utils"
LICENSE = "GPLv2"
DEPENDS = "alsa-lib ncurses"

LIC_FILES_CHKSUM = "file://hdsploader/COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

SRC_URI = "ftp://ftp.alsa-project.org/pub/tools/alsa-tools-${PV}.tar.bz2"

SRC_URI[md5sum] = "6b9d146af621dd712472b3ebb519c596"
SRC_URI[sha256sum] = "2a05047363b20f16820f36198ed3b139d1c55f55464750ea11d350e00b09cb86"


inherit autotools
