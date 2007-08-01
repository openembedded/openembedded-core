DESCRIPTION = "GNU Make examines the timestamps on a set of \
interdependent files, and, if necessary, issues commands \
to bring them up-to-date."
HOMEPAGE = "http://www.gnu.org/software/make/"
SECTION = "devel"
LICENSE = "GPL"

SRC_URI = "${GNU_MIRROR}/make/make-${PV}.tar.bz2 \
           file://SCCS.patch;patch=1"

inherit autotools
