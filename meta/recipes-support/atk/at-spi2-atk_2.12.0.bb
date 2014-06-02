SUMMARY = "AT-SPI 2 Toolkit Bridge"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "aafc5e1bb17cd3ad440f6350e7a021c1"
SRC_URI[sha256sum] = "3b5467c9f169812bc36a4245355b7deedea0a62eb22153df96ced88dcd1c3633"

DEPENDS = "dbus glib-2.0 atk at-spi2-core"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-gnome ${PN}-gtk2"

FILES_${PN}-gnome = "${libdir}/gnome-settings-daemon-3.0/gtk-modules"
FILES_${PN}-gtk2 = "${libdir}/gtk-2.0/modules/libatk-bridge.*"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"
