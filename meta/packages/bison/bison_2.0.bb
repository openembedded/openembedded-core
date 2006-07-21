DESCRIPTION = "GNU Project parser generator (yacc replacement)."
HOMEPAGE = "http://www.gnu.org/software/bison/"
LICENSE = "GPL"
SECTION = "devel"
PRIORITY = "optional"

SRC_URI = "${GNU_MIRROR}/bison/bison-${PV}.tar.gz \
	   file://m4.patch;patch=1"

inherit autotools
