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
                file://dont-depend-on-help2man.patch \
	       "

# No point in hardcoding path to m4, just use PATH
EXTRA_OECONF += "M4=m4"

SRC_URI = "${BASE_SRC_URI} \
           file://fix_cross_manpage_building.patch \
          "

SRC_URI[md5sum] = "ded660799e76fb1667d594de1f7a0da9"
SRC_URI[sha256sum] = "19bbe7374fd602f7a6654c131c21a15aebdc06cc89493e8ff250cb7f9ed0a831"

LDFLAGS_prepend_libc-uclibc = " -lrt "
DEPENDS_class-native = "gettext-minimal-native"
SRC_URI_class-native = "${BASE_SRC_URI}"

inherit autotools gettext
acpaths = "-I ${S}/m4"

do_install_append_class-native() {
	create_wrapper ${D}/${bindir}/bison \
		BISON_PKGDATADIR=${STAGING_DATADIR_NATIVE}/bison
}
BBCLASSEXTEND = "native nativesdk"
