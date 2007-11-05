require usbpath_svn.bb
inherit native
DEPENDS = "libusb-native"

do_stage () {
	autotools_stage_all
}

