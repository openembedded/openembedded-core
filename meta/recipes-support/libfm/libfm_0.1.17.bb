DESCRIPTION = "Library for file management"
HOMEPAGE = "http://pcmanfm.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/fm.h;endline=22;md5=e64555171770a551e3b51cc06fc62f1a \
                    file://src/base/fm-config.h;endline=23;md5=ad0fc418c3cf041eea35ddb3daf37f17"

SECTION = "x11/libs"
DEPENDS = "glib-2.0 pango gtk+ menu-cache intltool-native"

PR = "r3"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/libfm-${PV}.tar.gz"

SRC_URI[md5sum] = "a97e03d973e6ac727f28d0934d6c9ad5"
SRC_URI[sha256sum] = "1740681cff4cd4c5a2eaa9805d8898269cfb6a49a0bda0acb242def15bc7131b"

inherit autotools pkgconfig

do_install_append () {
	rmdir ${D}${libdir}/gio/modules/
	rmdir ${D}${libdir}/gio/
}

PACKAGES += "${PN}-mime"
FILES_${PN}-mime = "${datadir}/mime/"
