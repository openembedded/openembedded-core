SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/include/main.h;endline=24;md5=c2242df552c880280315989bab626b90"

DEPENDS = "startup-notification gtk+ gconf gdk-pixbuf-csource-native"
PR = "r3"

inherit gnome update-alternatives

ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/metacity"
ALTERNATIVE_PRIORITY = "10"

EXTRA_OECONF += "--disable-verbose \
	         --disable-xinerama"

FILES_${PN} += "${datadir}/themes"

SRC_URI[archive.md5sum] = "8cb6d02cf66a1003532b4f5d2754d696"
SRC_URI[archive.sha256sum] = "3c670b41a214311006dc05f9a005696b9d3fdcb5c80f1275367416600103b3bf"
