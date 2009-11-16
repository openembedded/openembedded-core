SRC_URI_append_build-darwin = "?   file://non-gnu.patch;patch=1 "
RDEPENDS_${PN} = "diffstat-native patch-native bzip2-native util-linux-native"

INHIBIT_AUTOTOOLS_DEPS = "1"

inherit native

PATCHTOOL = "patch"
EXTRA_OECONF = "--disable-nls"

do_configure () {
	oe_runconf
}
