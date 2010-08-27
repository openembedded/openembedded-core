BROKEN = "1"

DESCRIPTION = "Alsa Tools"
SECTION = "console/utils"
LICENSE = "GPL"
DEPENDS = "alsa-lib ncurses"

SRC_URI = "ftp://ftp.alsa-project.org/pub/tools/alsa-tools-${PV}.tar.bz2"

inherit autotools
