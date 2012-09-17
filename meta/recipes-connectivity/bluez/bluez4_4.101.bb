require bluez4.inc

PR = "r1"

SRC_URI += "file://bluetooth.conf \
            file://sbc_mmx.patch"

SRC_URI[md5sum] = "fb42cb7038c380eb0e2fa208987c96ad"
SRC_URI[sha256sum] = "59738410ade9f0e61a13c0f77d9aaffaafe49ba9418107e4ad75fe52846f7487"

do_install_append() {
	install -m 0644 ${S}/audio/audio.conf ${D}/${sysconfdir}/bluetooth/
	install -m 0644 ${S}/network/network.conf ${D}/${sysconfdir}/bluetooth/
	install -m 0644 ${S}/input/input.conf ${D}/${sysconfdir}/bluetooth/
	# at_console doesn't really work with the current state of OE, so punch some more holes so people can actually use BT
	install -m 0644 ${WORKDIR}/bluetooth.conf ${D}/${sysconfdir}/dbus-1/system.d/
}

RDEPENDS_${PN}-dev = "bluez-hcidump"

ALLOW_EMPTY_libasound-module-bluez = "1"
PACKAGES =+ "libasound-module-bluez"

FILES_libasound-module-bluez = "${libdir}/alsa-lib/lib*.so ${datadir}/alsa"
FILES_${PN} += "${libdir}/bluetooth/plugins ${libdir}/bluetooth/plugins/*.so ${base_libdir}/udev/ ${systemd_unitdir}/ ${datadir}/dbus-1"
FILES_${PN}-dev += "\
  ${libdir}/bluetooth/plugins/*.la \
  ${libdir}/alsa-lib/*.la \
"

FILES_${PN}-dbg += "\
  ${libdir}/bluetooth/plugins/.debug \
  ${libdir}/*/.debug \
  ${base_libdir}/udev/.debug \
  "
