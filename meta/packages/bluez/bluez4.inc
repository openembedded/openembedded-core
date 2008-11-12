DESCRIPTION = "BlueZ - Bluetooth protocol stack for Linux"
SECTION = "console"
PRIORITY = "optional"
DEPENDS = "gstreamer gst-plugins-base libusb dbus"
PROVIDES = "bluez-utils-dbus"
RPROVIDES_${PN} = "bluez-pan bluez-sdp bluez-utils-dbus"
RREPLACES_${PN} = "bluez-utils-dbus"
RCONFLICTS_${PN} = "bluez-utils-nodbus"
LICENSE = "GPL"

SRC_URI = "http://www.kernel.org/pub/linux/bluetooth/bluez-${PV}.tar.gz"

S = "${WORKDIR}/bluez-${PV}"

inherit autotools update-rc.d pkgconfig

INITSCRIPT_NAME = "bluetooth"
INITSCRIPT_PARAMS = "defaults 23 19"

do_install_append() {
	install -d ${D}${base_sbindir} ${D}${base_bindir}/
	echo base_sbindir is:   ${D}${base_sbindir}	
	mv ${D}${sbindir}/* ${D}${base_sbindir}/
	mv ${D}${bindir}/* ${D}${base_bindir}/
	rmdir ${D}${bindir} ${D}${sbindir}
	chmod u+s ${D}${base_sbindir}/hciattach ${D}${base_sbindir}/hciconfig
}

PACKAGES =+ "${PN}-alsa ${PN}-alsa-dev gst-plugin-bluez gst-plugin-bluez-dev ${PN}-lib ${PN}-lib-dev ${PN}-lib-dbg"

FILES_${PN}-alsa = " ${libdir}/alsa-lib/*.so "
FILES_${PN}-alsa-dev += " ${libdir}/alsa-lib/*.la "

FILES_gst-plugin-bluez = "${libdir}/gstreamer-*/*.so"
FILES_gst-plugin-bluez-dev = "${libdir}/gstreamer-*/*.la"
FILES_${PN}-lib = "${libdir}/libbluetooth.so.*"
FILES_${PN}-lib-dev = "${libdir}/libbluetooth.so \
		       ${libdir}/*.la \
                       ${includedir}/bluetooth \ 
		       ${libdir}/pkgconfig/bluez.pc \
                      "
