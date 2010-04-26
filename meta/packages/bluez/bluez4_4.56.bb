DESCRIPTION = "Linux Bluetooth Stack Userland V4"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "gst-plugins-base alsa-lib libusb-compat dbus-glib"
HOMEPAGE = "http://www.bluez.org"
LICENSE = "GPL"

# For angstrom we want this to replace at least bluez-libs
PROVIDES_append_angstrom = " bluez-utils bluez-libs"

ASNEEDED = ""

SRC_URI = "\
  http://www.kernel.org/pub/linux/bluetooth/bluez-${PV}.tar.gz \
  file://fix-dfutool-usb-declaration-mismatch.patch;patch=1 \
  file://sbc-thumb.patch;patch=1 \
  file://bluetooth.conf \
"
S = "${WORKDIR}/bluez-${PV}"

inherit autotools
AUTOTOOLS_STAGE_PKGCONFIG = "1"

EXTRA_OECONF = "\
  --enable-gstreamer \
  --enable-alsa \
  --enable-usb \
  --enable-netlink \
  --enable-tools \
  --enable-bccmd \
  --enable-hid2hci \
  --enable-dfutool \
  --enable-hidd \
  --enable-pandd \
  --enable-dund \
  --disable-cups \
  --enable-test \
  --enable-manpages \
  --enable-configfiles \
  --enable-initscripts \
  --disable-pcmciarules \
"

do_install_append() {
        install -m 0644 ${S}/audio/audio.conf ${D}/${sysconfdir}/bluetooth/
        install -m 0644 ${S}/network/network.conf ${D}/${sysconfdir}/bluetooth/
        install -m 0644 ${S}/input/input.conf ${D}/${sysconfdir}/bluetooth/
        # at_console doesn't really work with the current state of OE, so punch some more holes so people can actually use BT
        install -m 0644 ${WORKDIR}/bluetooth.conf ${D}/${sysconfdir}/dbus-1/system.d/
}

PACKAGES =+ "gst-plugin-bluez libasound-module-bluez"

FILES_gst-plugin-bluez = "${libdir}/gstreamer-0.10/lib*.so"
FILES_libasound-module-bluez = "${libdir}/alsa-lib/lib*.so"
FILES_${PN} += "${libdir}/bluetooth/plugins/*.so"
FILES_${PN}-dev += "\
  ${libdir}/bluetooth/plugins/*.la \
  ${libdir}/alsa-lib/*.la \
  ${libdir}/gstreamer-0.10/*.la \
"

FILES_${PN}-dbg += "\
  ${libdir}/bluetooth/plugins/.debug \
  ${libdir}/*/.debug \
"
