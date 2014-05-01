SUMMARY = "GNU Project parser generator (yacc replacement)"
DESCRIPTION = "Bison is a general-purpose parser generator that converts an annotated context-free grammar into \
an LALR(1) or GLR parser for that grammar.  Bison is upward compatible with Yacc: all properly-written Yacc \
grammars ought to work with Bison with no change. Anyone familiar with Yacc should be able to use Bison with \
little trouble."
HOMEPAGE = "http://www.gnu.org/software/bison/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"
SECTION = "devel"
DEPENDS = "bison-native flex-native"

BASE_SRC_URI = "${GNU_MIRROR}/bison/bison-${PV}.tar.xz \
	        file://m4.patch \
                file://dont-depend-on-help2man.patch \
	       "

# No point in hardcoding path to m4, just use PATH
EXTRA_OECONF += "M4=m4"

SRC_URI = "${BASE_SRC_URI} \
           file://fix_cross_manpage_building.patch \
          "

SRC_URI[md5sum] = "7be02eb973eccf388f1ae750fc09eed0"
SRC_URI[sha256sum] = "b409adcbf245baadb68d2f66accf6fdca5e282cafec1b865f4b5e963ba8ea7fb"

LDFLAGS_prepend_libc-uclibc = " -lrt "
DEPENDS_class-native = "gettext-minimal-native"
SRC_URI_class-native = "${BASE_SRC_URI}"

inherit autotools gettext texinfo
acpaths = "-I ${S}/m4"

do_install_append_class-native() {
	create_wrapper ${D}/${bindir}/bison \
		BISON_PKGDATADIR=${STAGING_DATADIR_NATIVE}/bison
}
BBCLASSEXTEND = "native nativesdk"
