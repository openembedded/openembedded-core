DESCRIPTION = "Linux Bluetooth Stack Userland Utilities."
SECTION = "console"
PRIORITY = "optional"
DEPENDS = "gstreamer gst-plugins-base bluez-libs-${PV} libusb dbus"
PROVIDES = "bluez-utils-dbus"
RPROVIDES_${PN} = "bluez-pan bluez-sdp bluez-utils-dbus"
RREPLACES_${PN} = "bluez-utils-dbus"
RCONFLICTS_${PN} = "bluez-utils-nodbus"
LICENSE = "GPL"

SRC_URI = "http://bluez.sourceforge.net/download/bluez-utils-${PV}.tar.gz \
           file://hcid.conf "

# Almost all serial CF cards w/ manfid 0x0000,0x0000 seem to use the bcs protocol
# Let's default to that instead of 'any' until further notice...
#SRC_URI += " file://default-manfid-0x0-to-bcps.patch;patch=1"

S = "${WORKDIR}/bluez-utils-${PV}"

inherit autotools update-rc.d

INITSCRIPT_NAME = "bluetooth"
INITSCRIPT_PARAMS = "defaults 23 19"

do_install_append() {
	install -d ${D}${base_sbindir} ${D}${base_bindir}/  ${D}${sysconfdir}/apm/event.d/
	mv ${D}${sbindir}/* ${D}${base_sbindir}/
	mv ${D}${bindir}/* ${D}${base_bindir}/
	rmdir ${D}${bindir} ${D}${sbindir}
	chmod u+s ${D}${base_sbindir}/hciattach ${D}${base_sbindir}/hciconfig
	install -m 0644 ${WORKDIR}/hcid.conf ${D}${sysconfdir}/bluetooth/
        install -m 0755 ${S}/daemon/.libs/passkey-agent ${D}${base_bindir}/
}

PACKAGES =+ "${PN}-alsa ${PN}-alsa-dev gst-plugin-bluez gst-plugin-bluez-dbg"

FILES_${PN}-alsa = "${libdir}/alsa-lib/libasound*"
FILES_${PN}-alsa-dev += " ${libdir}/alsa-lib/*.la "

FILES_gst-plugin-bluez = "${libdir}/gstreamer*/libgstblue*.so"
FILES_gst-plugin-bluez-dbg = "${libdir}/gstreamer*/.debug/libgstblue*.so"

#
# CUPS support
#
#PACKAGES =+ " bluez-cups-backend"
#FILES_bluez-cups-backend = "${libdir}/cups/backend/bluetooth"
#RDEPENDS_bluez-cups-backend = "cups"
