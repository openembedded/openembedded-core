DESCRIPTION = "gThumb is an image viewer and browser for the GNOME Desktop"
SECTION = "x11/gnome"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
DEPENDS = "glib-2.0 gtk+ libxml2 gnome-doc-utils libunique gconf libpng gstreamer jpeg tiff gst-plugins-base"
PR = "r1"

EXTRA_OECONF = "--disable-gnome-keyring --disable-libsoup --disable-exiv2 --disable-clutter"

inherit gnome pkgconfig

FILES_${PN} += "${datadir}/icons"
FILES_${PN}-dbg += "${libdir}/gthumb/modules/.debug/ ${libdir}/gthumb/extensions/.debug/"

SRC_URI[archive.md5sum] = "bfa0957a1bafc5d049292cd5fad372ce"
SRC_URI[archive.sha256sum] = "0060c05fd372c2af2048d294ab3f75c8fb02c576d3e2004e08b7d34f49da1e66"
