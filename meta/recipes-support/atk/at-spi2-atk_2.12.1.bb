SUMMARY = "AT-SPI 2 Toolkit Bridge"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "ae11df528f1f038987797f39c8357f81"
SRC_URI[sha256sum] = "5fa9c527bdec028e06797563cd52d49bcf06f638549df983424d88db89bb1336"

DEPENDS = "dbus glib-2.0 atk at-spi2-core"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-gnome ${PN}-gtk2"

FILES_${PN}-gnome = "${libdir}/gnome-settings-daemon-3.0/gtk-modules"
FILES_${PN}-gtk2 = "${libdir}/gtk-2.0/modules/libatk-bridge.*"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"
