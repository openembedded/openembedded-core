SECTION = "base"
LICENSE = "BSD"
include fontconfig_${PV}.bb
inherit native
DEPENDS = "freetype-native expat-native zlib-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/fontconfig-${PV}"

EXTRA_OEMAKE = ""

do_stage () {
	oe_runmake install
	install fc-lang/fc-lang ${STAGING_BINDIR}
	install fc-glyphname/fc-glyphname ${STAGING_BINDIR}
}
