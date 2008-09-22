LICENSE = "GPL"
SECTION = "x11/gnome"
DEPENDS = "icon-naming-utils-native glib-2.0 intltool-native"
RDEPENDS = "hicolor-icon-theme"

FILES_${PN} += "${datadir}/*"

EXTRA_OECONF = "--disable-hicolor-check"

inherit gnome

PACKAGE_ARCH = "all"
