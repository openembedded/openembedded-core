PR = "r0"

SRC_URI = "${GNU_MIRROR}/ncurses/ncurses-${PV}.tar.gz \
           file://makefile_tweak.patch;patch=1 \
	   file://configure_fix.patch;patch=1"
#           file://visibility.patch;patch=1"
S = "${WORKDIR}/ncurses-${PV}"

DEFAULT_PREFERENCE = "-1"

require ncurses.inc
