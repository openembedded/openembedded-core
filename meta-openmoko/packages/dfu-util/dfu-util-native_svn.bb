require dfu-util_${PV}.bb

inherit native deploy

DEPENDS = "libusb-native usbpath-native"

do_deploy() {
	install -m 0755 src/dfu-util_static ${DEPLOYDIR}/dfu-util
}

addtask deploy before do_package after do_install
