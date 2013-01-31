DESCRIPTION = "Library for file management"
HOMEPAGE = "http://pcmanfm.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/fm.h;endline=22;md5=e64555171770a551e3b51cc06fc62f1a \
                    file://src/base/fm-config.h;endline=23;md5=ad0fc418c3cf041eea35ddb3daf37f17"

SECTION = "x11/libs"
DEPENDS = "glib-2.0 pango gtk+ menu-cache intltool-native libexif"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/libfm-${PV}.tar.gz \
           file://fix-make-parallelism-issue.patch \
           "

SRC_URI[md5sum] = "a5bc8b8291cf810c659bfb3af378b5de"
SRC_URI[sha256sum] = "b9426e588670b53570b808c49abd1d103863614dd3622559b8c3ef1392fe0b3d"

inherit autotools pkgconfig

PACKAGES += "${PN}-mime"
FILES_${PN}-mime = "${datadir}/mime/"
