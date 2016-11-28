SUMMARY = "WebKit based web browser for GNOME"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"

DEPENDS = "libsoup-2.4 webkitgtk gtk+3 iso-codes avahi libnotify gcr \
	   gsettings-desktop-schemas gnome-desktop3 libxml2-native intltool-native"

inherit gnomebase gsettings distro_features_check upstream-version-is-even
REQUIRED_DISTRO_FEATURES = "x11"

SRC_URI += "file://0001-yelp.m4-drop-the-check-for-itstool.patch"
SRC_URI[archive.md5sum] = "bfb6347acb91163aec3883bb3397372f"
SRC_URI[archive.sha256sum] = "a11fe3495009f776e354bcafac3ab8fc52788f5bdb9d5ee2c2f1f2f021fb49c2"

EXTRA_OECONF += " --with-distributor-name=${DISTRO}"

do_configure_prepend() {
    sed -i -e s:help::g ${S}/Makefile.am
}

FILES_${PN} += "${datadir}/appdata ${datadir}/dbus-1 ${datadir}/gnome-shell/search-providers"
RDEPENDS_${PN} = "iso-codes adwaita-icon-theme"
