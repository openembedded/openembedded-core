require time.inc

SRC_URI = "${GNU_MIRROR}/time/time-${PV}.tar.gz \
	   file://debian.patch;patch=1"
#	   file://autofoo.patch;patch=1 \
#	   file://compile.patch;patch=1"
S = "${WORKDIR}/time-${PV}"

inherit autotools
