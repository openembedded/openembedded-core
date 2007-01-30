SECTION = "base"
LICENSE = "BSD"
require fontconfig_${PV}.bb
inherit native
DEPENDS = "freetype-native expat-native zlib-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/fontconfig-${PV}"
SRC_URI += " file://stop-fc-cache.patch;patch=1"

EXTRA_OEMAKE = ""
EXTRA_OECONF += "--with-freetype-config=${STAGING_BINDIR}/freetype-config"

do_stage () {
	oe_runmake install
	install fc-lang/fc-lang ${STAGING_BINDIR}
	install fc-glyphname/fc-glyphname ${STAGING_BINDIR}
}
