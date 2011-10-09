DESCRIPTION = "Library for file management"
HOMEPAGE = "http://pcmanfm.sourceforge.net/"
BUGTRACKER = ""

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/fm.h;endline=22;md5=e64555171770a551e3b51cc06fc62f1a \
                    file://src/base/fm-config.h;endline=23;md5=ad0fc418c3cf041eea35ddb3daf37f17"

SECTION = "x11/libs"
DEPENDS = "gtk+ menu-cache intltool-native"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/libfm-${PV}.tar.gz \
           file://configure_fix.patch"

SRC_URI[md5sum] = "c09bce415ff6dc2dd835e28aeddeabe3"
SRC_URI[sha256sum] = "ed49365319b941757c155b3790a2adf73f5072227c578d9bcba7329c6f9fcd3b"

inherit autotools pkgconfig
