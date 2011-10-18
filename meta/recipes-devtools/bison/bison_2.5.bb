SUMMARY = "GNU Project parser generator (yacc replacement)."
DESCRIPTION = "Bison is a general-purpose parser generator that converts an annotated context-free grammar into \
an LALR(1) or GLR parser for that grammar.  Bison is upward compatible with Yacc: all properly-written Yacc \
grammars ought to work with Bison with no change. Anyone familiar with Yacc should be able to use Bison with \
little trouble."
HOMEPAGE = "http://www.gnu.org/software/bison/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SECTION = "devel"
DEPENDS = "bison-native flex-native"

PR = "r1"

BASE_SRC_URI = "${GNU_MIRROR}/bison/bison-${PV}.tar.gz \
	   file://m4.patch \
	  "

SRC_URI = "${BASE_SRC_URI} \
        file://fix_cross_manpage_building.patch "

SRC_URI[md5sum] = "687e1dcd29452789d34eaeea4c25abe4"
SRC_URI[sha256sum] = "722def46e4a19a5b7a579ef30db1965f86c37c1a20a5f0113743a2e4399f7c99"

DEPENDS_virtclass-native = "gettext-minimal-native"
SRC_URI_virtclass-native = "${BASE_SRC_URI}"

inherit autotools gettext
acpaths = "-I ${S}/m4"

do_install_append_virtclass-native() {
	create_wrapper ${D}/${bindir}/bison \
		M4=${STAGING_BINDIR_NATIVE}/m4 \
		BISON_PKGDATADIR=${STAGING_DATADIR_NATIVE}/bison
}
#bison-native encodes the staging M4 path
BBCLASSEXTEND = "native"
