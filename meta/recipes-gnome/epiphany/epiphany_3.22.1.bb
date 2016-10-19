SUMMARY = "WebKit based web browser for GNOME"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "libsoup-2.4 webkitgtk gtk+3 iso-codes ca-certificates avahi libnotify gcr libwnck3 \
	   gsettings-desktop-schemas gnome-desktop3 libxml2-native intltool-native"

inherit gnomebase gsettings distro_features_check upstream-version-is-even
# libwnck3 is x11 only
REQUIRED_DISTRO_FEATURES = "x11"

SRC_URI += "file://0001-yelp.m4-drop-the-check-for-itstool.patch"
SRC_URI[archive.md5sum] = "9b9b73601a32f5b11c02411952cec27a"
SRC_URI[archive.sha256sum] = "aab162ede54d71e583e382ab5e3567f28d81e0cd42719a11cad8008b56c5cc0e"

EXTRA_OECONF += " --with-distributor-name=${DISTRO}"

do_configure_prepend() {
    sed -i -e s:help::g ${S}/Makefile.am
}

FILES_${PN} += "${datadir}/appdata ${datadir}/dbus-1 ${datadir}/gnome-shell/search-providers"
RDEPENDS_${PN} = "iso-codes adwaita-icon-theme"
RRECOMMENDS_${PN} = "ca-certificates"

