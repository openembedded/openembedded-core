SUMMARY = "Linux Bluetooth Stack Userland V5"
DESCRIPTION = "Linux Bluetooth stack V5 userland components.  These include a system configurations, daemons, tools and system libraries."
HOMEPAGE = "http://www.bluez.org"
SECTION = "libs"
LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://COPYING.LIB;md5=fb504b67c50331fc78734fed90fb0e09 \
                    file://src/main.c;beginline=1;endline=24;md5=9bc54b93cd7e17bf03f52513f39f926e"
DEPENDS = "udev libusb dbus-glib glib-2.0 libcheck readline"
PROVIDES += "bluez-hcidump"
RPROVIDES_${PN} += "bluez-hcidump"

RCONFLICTS_${PN} = "bluez4"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'alsa', 'alsa', '', d)} obex-profiles"
PACKAGECONFIG[obex-profiles] = "--enable-obex,--disable-obex,libical"

SRC_URI = "\
    ${KERNELORG_MIRROR}/linux/bluetooth/bluez-${PV}.tar.xz \
    file://bluetooth.conf \
"
S = "${WORKDIR}/bluez-${PV}"

inherit autotools-brokensep pkgconfig systemd

EXTRA_OECONF = "\
  --enable-tools \
  --disable-cups \
  --enable-test \
  --enable-datafiles \
  ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemdunitdir=${systemd_unitdir}/system/', '--disable-systemd', d)} \
  --enable-library \
"

do_install_append() {
	install -d ${D}${sysconfdir}/bluetooth/
	if [ -f ${S}/profiles/audio/audio.conf ]; then
	    install -m 0644 ${S}/profiles/audio/audio.conf ${D}/${sysconfdir}/bluetooth/
	fi
	if [ -f ${S}/profiles/network/network.conf ]; then
	    install -m 0644 ${S}/profiles/network/network.conf ${D}/${sysconfdir}/bluetooth/
	fi
	if [ -f ${S}/profiles/input/input.conf ]; then
	    install -m 0644 ${S}/profiles/input/input.conf ${D}/${sysconfdir}/bluetooth/
	fi
	# at_console doesn't really work with the current state of OE, so punch some more holes so people can actually use BT
	install -m 0644 ${WORKDIR}/bluetooth.conf ${D}/${sysconfdir}/dbus-1/system.d/
}

ALLOW_EMPTY_libasound-module-bluez = "1"
PACKAGES =+ "libasound-module-bluez ${PN}-testtools ${PN}-obex"

FILES_libasound-module-bluez = "${libdir}/alsa-lib/lib*.so ${datadir}/alsa"
FILES_${PN} += "${libdir}/bluetooth/plugins ${libdir}/bluetooth/plugins/*.so ${base_libdir}/udev/ ${nonarch_base_libdir}/udev/ ${systemd_unitdir}/ ${datadir}/dbus-1"
FILES_${PN}-dev += "\
  ${libdir}/bluetooth/plugins/*.la \
  ${libdir}/alsa-lib/*.la \
"

FILES_${PN}-obex = "${libdir}/bluez5/bluetooth/obexd \
                    ${exec_prefix}/lib/systemd/user/obex.service \
                    ${datadir}/dbus-1/services/org.bluez.obex.service \
                   "
SYSTEMD_SERVICE_${PN}-obex = "obex.service"

FILES_${PN}-testtools = "${libdir}/bluez/test/*"

FILES_${PN}-dbg += "\
  ${libdir}/${BPN}/bluetooth/.debug \
  ${libdir}/bluetooth/plugins/.debug \
  ${libdir}/*/.debug \
  */udev/.debug \
  "

RDEPENDS_${PN}-testtools += "python python-dbus python-pygobject"

SYSTEMD_SERVICE_${PN} = "bluetooth.service"

EXCLUDE_FROM_WORLD = "1"
