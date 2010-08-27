DESCRIPTION = "This library provides weak aliases for pthread functions \
not provided in libc or otherwise available by default."
HOMEPAGE = "http://xcb.freedesktop.org"
BUGTRACKER = "http://bugs.freedesktop.org/buglist.cgi?product=XCB"
SECTION = "x11/libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=6edc1fea03d959f0c2d743fe5ca746ad"
PR = "r0"

PARALLEL_MAKE = ""
#DEPENDS = "xcb-proto xproto libxau libxslt-native"
# DEPENDS += "xsltproc-native gperf-native"

SRC_URI = "http://xcb.freedesktop.org/dist/libpthread-stubs-${PV}.tar.bz2"

inherit autotools pkgconfig

RDEPENDS_${PN}-dev = ""
RRECOMMENDS_${PN}-dbg = "${PN}-dev (= ${EXTENDPV})"
