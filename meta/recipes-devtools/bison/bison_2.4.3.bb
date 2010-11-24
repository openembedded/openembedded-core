DESCRIPTION = "GNU Project parser generator (yacc replacement)."
HOMEPAGE = "http://www.gnu.org/software/bison/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SECTION = "devel"
PRIORITY = "optional"
DEPENDS = "gettext bison-native"

PR = "r0"

BASE_SRC_URI = "${GNU_MIRROR}/bison/bison-${PV}.tar.gz \
	   file://m4.patch;patch=1"

SRC_URI = "${BASE_SRC_URI} \
        file://fix_cross_manpage_building.patch "

DEPENDS_virtclass-native = "gettext-native"
SRC_URI_virtclass-native = "${BASE_SRC_URI}"

inherit autotools
acpaths = "-I ${S}/m4"

BBCLASSEXTEND = "native"
