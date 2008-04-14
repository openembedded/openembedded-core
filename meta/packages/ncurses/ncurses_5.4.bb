PR = "r10"

SRC_URI = "${GNU_MIRROR}/ncurses/ncurses-${PV}.tar.gz \
           file://makefile_tweak.patch;patch=1 \
           file://visibility.patch;patch=1"
S = "${WORKDIR}/ncurses-${PV}"

require ncurses.inc
