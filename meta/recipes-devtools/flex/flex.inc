SUMMARY = "Flex (The Fast Lexical Analyzer)"
DESCRIPTION = "Flex is a fast lexical analyser generator.  Flex is a tool for generating programs that recognize \
lexical patterns in text."
HOMEPAGE = "http://sourceforge.net/projects/flex/"

SECTION = "devel"
LICENSE = "BSD"

SRC_URI = "${SOURCEFORGE_MIRROR}/flex/flex-${PV}.tar.bz2 "

inherit autotools gettext

M4 = "${bindir}/m4"
M4_class-native = "${STAGING_BINDIR_NATIVE}/m4"

do_install_append() {
	create_wrapper ${D}/${bindir}/flex M4=${M4}
}
