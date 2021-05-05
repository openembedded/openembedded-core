SUMMARY = "GNU Project parser generator (yacc replacement)"
DESCRIPTION = "Bison is a general-purpose parser generator that converts an annotated context-free grammar into \
an LALR(1) or GLR parser for that grammar.  Bison is upward compatible with Yacc: all properly-written Yacc \
grammars ought to work with Bison with no change. Anyone familiar with Yacc should be able to use Bison with \
little trouble."
HOMEPAGE = "http://www.gnu.org/software/bison/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ebbd3e34237af26da5dc08a4e440464"
SECTION = "devel"
DEPENDS = "bison-native flex-native"

SRC_URI = "${GNU_MIRROR}/bison/bison-${PV}.tar.xz \
           file://add-with-bisonlocaledir.patch \
           file://0001-Use-mapped-file-name-for-symbols.patch \
           "
SRC_URI[sha256sum] = "67d68ce1e22192050525643fc0a7a22297576682bef6a5c51446903f5aeef3cf"

# No point in hardcoding path to m4, just use PATH
EXTRA_OECONF += "M4=m4"

inherit autotools gettext texinfo

# The automatic m4 path detection gets confused, so force the right value
acpaths = "-I ./m4"

do_compile_prepend() {
	for i in mfcalc calc++ rpcalc; do mkdir -p ${B}/examples/$i; done
}

do_install_append_class-native() {
	create_wrapper ${D}/${bindir}/bison \
		BISON_PKGDATADIR=${STAGING_DATADIR_NATIVE}/bison
}
do_install_append_class-nativesdk() {
	create_wrapper ${D}/${bindir}/bison \
		BISON_PKGDATADIR=${datadir}/bison
}
BBCLASSEXTEND = "native nativesdk"
