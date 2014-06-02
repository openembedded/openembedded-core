SUMMARY = "Library for file management"
HOMEPAGE = "http://pcmanfm.sourceforge.net/"

LICENSE = "GPLv2 & GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/fm.h;endline=22;md5=e64555171770a551e3b51cc06fc62f1a \
                    file://src/base/fm-config.h;endline=23;md5=ad0fc418c3cf041eea35ddb3daf37f17"

SECTION = "x11/libs"
DEPENDS = "glib-2.0 pango gtk+ menu-cache intltool-native libexif"

SRC_URI = "${SOURCEFORGE_MIRROR}/pcmanfm/libfm-${PV}.tar.gz \
           file://fix-make-parallelism-issue.patch \
           file://ignore_automake_warnings.patch \
           "

SRC_URI[md5sum] = "ea3d09b23ef4c37cb84ae57ea16b8f08"
SRC_URI[sha256sum] = "158e2b6974350d2dab15932b496bb4d448553e60bbf7cdfe4d6e9bd99d19d682"

inherit autotools-brokensep pkgconfig gtk-doc

do_configure[dirs] =+ "${S}/m4"

PACKAGES += "${PN}-mime"
FILES_${PN}-mime = "${datadir}/mime/"
