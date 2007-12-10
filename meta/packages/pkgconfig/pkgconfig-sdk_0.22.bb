require pkgconfig.inc
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/pkgconfig-${PV}"

S = "${WORKDIR}/pkg-config-${PV}/"
inherit sdk
DEPENDS = ""

do_configure() {
        gnu-configize
	libtoolize --force
	oe_runconf
}
