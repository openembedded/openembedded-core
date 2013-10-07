SUMMARY = "AT-SPI 2 Toolkit Bridge"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "38e4f7e2898f7ba3cc3ec269d9402789"
SRC_URI[sha256sum] = "dea7ff2f9bc9bbdb0351112616d738de718b55739cd2511afecac51604c31a94"

DEPENDS = "dbus glib-2.0 atk at-spi2-core"

inherit autotools

PACKAGES =+ "${PN}-gnome ${PN}-gtk2"

FILES_${PN}-gnome = "${libdir}/gnome-settings-daemon-3.0/gtk-modules"
FILES_${PN}-gtk2 = "${libdir}/gtk-2.0/modules/libatk-bridge.*"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"
