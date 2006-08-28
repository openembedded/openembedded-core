require quilt_${PV}.inc

SRC_URI_append_build-darwin = "?   file://non-gnu.patch;patch=1 "
RDEPENDS_${PN} = "diffstat-native patch-native bzip2-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/quilt-${PV}"

INHIBIT_AUTOTOOLS_DEPS = "1"

inherit autotools native

PATCHTOOL = "patch"
EXTRA_OECONF = "--disable-nls"

do_configure () {
	oe_runconf
}
