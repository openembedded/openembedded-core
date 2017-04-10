SUMMARY = "AT-SPI 2 Toolkit Bridge"
LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=e9f288ba982d60518f375b5898283886"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz"
SRC_URI[md5sum] = "984f27528b0e3ec1f153a901cd4fe497"
SRC_URI[sha256sum] = "022d68497c05cc65ba7cd2b166132de9a160c8d90e9200b5faa473ef7e784c61"

DEPENDS = "dbus glib-2.0 glib-2.0-native atk at-spi2-core"

inherit autotools pkgconfig distro_features_check upstream-version-is-even

# The at-spi2-core requires x11 in DISTRO_FEATURES
REQUIRED_DISTRO_FEATURES = "x11"

PACKAGES =+ "${PN}-gnome ${PN}-gtk2"

FILES_${PN}-gnome = "${libdir}/gnome-settings-daemon-3.0/gtk-modules"
FILES_${PN}-gtk2 = "${libdir}/gtk-2.0/modules/libatk-bridge.*"
