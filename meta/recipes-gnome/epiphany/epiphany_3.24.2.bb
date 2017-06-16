SUMMARY = "WebKit based web browser for GNOME"
LICENSE = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "libsoup-2.4 webkitgtk gtk+3 iso-codes avahi libnotify gcr \
	   gsettings-desktop-schemas gnome-desktop3 libxml2-native \
	   glib-2.0 glib-2.0-native json-glib"

inherit gnomebase gsettings distro_features_check upstream-version-is-even gettext
REQUIRED_DISTRO_FEATURES = "x11"

SRC_URI += "file://0001-yelp.m4-drop-the-check-for-itstool.patch"
SRC_URI[archive.md5sum] = "e035dc6f64f0c1909de823e03f16b2f3"
SRC_URI[archive.sha256sum] = "5abc0d0c60591df5236ac9b8979dc9f7d9acbb8ad0902b4772d2b7beea81c58d"

EXTRA_OECONF += " --with-distributor-name=${DISTRO}"

do_configure_prepend() {
    sed -i -e s:help::g ${S}/Makefile.am
}

FILES_${PN} += "${datadir}/dbus-1 ${datadir}/gnome-shell/search-providers"
RDEPENDS_${PN} = "iso-codes adwaita-icon-theme"
