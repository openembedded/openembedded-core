require bluez4.inc

PR = "r0"

SRC_URI += "file://bluetooth.conf"

SRC_URI[md5sum] = "362864b716950baa04797de735fc237b"
SRC_URI[sha256sum] = "9a5b655bada7c7a1921cb3bac83b8a32bbe49893e4c7a1377cdc1b0d35f7d233"

do_install_append() {
	install -m 0644 ${S}/audio/audio.conf ${D}/${sysconfdir}/bluetooth/
	install -m 0644 ${S}/network/network.conf ${D}/${sysconfdir}/bluetooth/
	install -m 0644 ${S}/input/input.conf ${D}/${sysconfdir}/bluetooth/
	# at_console doesn't really work with the current state of OE, so punch some more holes so people can actually use BT
	install -m 0644 ${WORKDIR}/bluetooth.conf ${D}/${sysconfdir}/dbus-1/system.d/
}

RDEPENDS_${PN}-dev = "bluez-hcidump"

PACKAGES =+ "libasound-module-bluez"

FILES_libasound-module-bluez = "${libdir}/alsa-lib/lib*.so ${datadir}/alsa"
FILES_${PN} += "${libdir}/bluetooth/plugins/*.so ${base_libdir}/udev/ ${base_libdir}/systemd/"
FILES_${PN}-dev += "\
  ${libdir}/bluetooth/plugins/*.la \
  ${libdir}/alsa-lib/*.la \
"

FILES_${PN}-dbg += "\
  ${libdir}/bluetooth/plugins/.debug \
  ${libdir}/*/.debug \
  ${base_libdir}/udev/.debug \
  "
