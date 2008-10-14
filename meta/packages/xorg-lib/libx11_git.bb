DEFAULT_PREFERENCE = "-1"

require libx11.inc
PV = "1.1.99.1+git${SRCREV}"

DEPENDS = "xproto xextproto xcmiscproto xf86bigfontproto kbproto inputproto \
           bigreqsproto xtrans libxau libxcb libxdmcp util-macros"
PV = "1.1.99.1+gitr${SRCREV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libX11;protocol=git"
S = "${WORKDIR}/git"

EXTRA_OECONF += "--enable-malloc0returnsnull"

do_compile() {
	(
		unset CC LD CXX CCLD
		oe_runmake -C src/util 'CC=${BUILD_CC}' 'LD=${BUILD_LD}' 'CXX=${BUILD_CXX}' 'CCLD=${BUILD_CCLD}' 'CFLAGS=-D_GNU_SOURCE ${BUILD_CFLAGS}' 'LDFLAGS=${BUILD_LDFLAGS}' 'CXXFLAGS=${BUILD_CXXFLAGS}' 'CPPFLAGS=${BUILD_CPPFLAGS}' makekeys
	)
	oe_runmake
}

do_stage() {
	autotools_stage_all
}
