DESCRIPTION = "gThumb is an image viewer and browser for the GNOME Desktop"
SECTION = "x11/gnome"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
DEPENDS = "glib-2.0 gtk+ libxml2 gnome-doc-utils libunique gconf libpng gstreamer jpeg tiff gst-plugins-base"

PR = "r2"

EXTRA_OECONF = "--disable-gnome-keyring --disable-libsoup --disable-exiv2 --disable-clutter"

PARALLEL_MAKEINST=""

inherit gnome pkgconfig

FILES_${PN} += "${datadir}/icons"
FILES_${PN}-dbg += "${libdir}/gthumb/modules/.debug/ ${libdir}/gthumb/extensions/.debug/"

SRC_URI[archive.md5sum] = "97fc13221b0c5d80c27a2e25a3a3ac6f"
SRC_URI[archive.sha256sum] = "cf809695230ab8892a078be454a42ade865754c72ec1da7c3d74d4310de54f1d"

do_install_append () {
	rm ${D}${libdir}/${BPN}/extensions/*.a
}
