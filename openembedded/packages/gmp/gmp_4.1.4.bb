SECTION = "libs"
DESCRIPTION = "GNU multiprecision arithmetic library"
HOMEPAGE = "http://www.swox.com/gmp/"
LICENSE = "GPL LGPL"

PR = "r1"

SRC_URI = "ftp://ftp.gnu.org/gnu/gmp/gmp-${PV}.tar.bz2 \
	   file://configure.patch;patch=1 \
	   file://amd64.patch;patch=1 \
	   file://sh4-asmfix.patch;patch=1"

inherit autotools 

acpaths = ""

do_stage () {
	autotools_stage_all
}
