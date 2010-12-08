DESCRIPTION = "gThumb is an image viewer and browser for the GNOME Desktop"
SECTION = "x11/gnome"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
DEPENDS = "glib-2.0 gtk+ libxml2 gnome-doc-utils libunique gconf-dbus libpng gstreamer jpeg tiff"
PR = "r1"

EXTRA_OECONF = "--disable-gnome-keyring --disable-libsoup --disable-exiv2 --disable-clutter"

inherit gnome pkgconfig

FILES_${PN} += "${datadir}/icons"
FILES_${PN}-dbg += "${libdir}/gthumb/modules/.debug"

SRC_URI[archive.md5sum] = "2911cd9b875efdfd554547176c59e309"
SRC_URI[archive.sha256sum] = "9bb32ee44647f3f934d41344e2c1dbbd1546bf4949824030b3b818545758118c"
