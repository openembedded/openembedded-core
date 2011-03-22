SECTION = "devel"
DESCRIPTION = "GNU multiprecision arithmetic library"
HOMEPAGE = "http://www.swox.com/gmp/"
LICENSE = "GPLv3 LGPLv3"

SRC_URI = "${GNU_MIRROR}/gmp/gmp-${PV}.tar.bz2 \
	   file://configure.patch \
	   file://amd64.patch "

inherit autotools

ARM_INSTRUCTION_SET = "arm"

acpaths = ""

BBCLASSEXTEND = "native nativesdk"
