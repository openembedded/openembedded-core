SECTION = "devel"
DESCRIPTION = "GNU Make examines the timestamps on a set of \
interdependent files, and, if necessary, issues commands \
to bring them up-to-date."
LICENSE = "GPL"
SRC_URI = "${GNU_MIRROR}/make/make-${PV}.tar.bz2 \
	   file://SCCS.patch;patch=1"

inherit autotools
