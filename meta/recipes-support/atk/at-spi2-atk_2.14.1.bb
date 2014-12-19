SUMMARY = "AT-SPI 2 Toolkit Bridge"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "5c6a085249a6d4e792ead86dca183504"
SRC_URI[sha256sum] = "058f34ea60edf0a5f831c9f2bdd280fe95c1bcafb76e466e44aa0fb356d17bcb"

DEPENDS = "dbus glib-2.0 atk at-spi2-core"

inherit autotools pkgconfig

PACKAGES =+ "${PN}-gnome ${PN}-gtk2"

FILES_${PN}-gnome = "${libdir}/gnome-settings-daemon-3.0/gtk-modules"
FILES_${PN}-gtk2 = "${libdir}/gtk-2.0/modules/libatk-bridge.*"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"
