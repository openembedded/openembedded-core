require libx11.inc

DESCRIPTION += " Support for XCB, and XCMS is disabled in this version."

LICENSE = "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=bf75bfe4d05068311b5e6862d4b5f2c5"

PR = "r1"

DEPENDS += "libxcb xproto xextproto xtrans libxau kbproto inputproto xf86bigfontproto xproto-native"

SRC_URI += "file://../libx11-${PV}/x11_disable_makekeys.patch \
            file://../libx11-${PV}/include_fix.patch \
            file://../libx11-${PV}/nodolt.patch \
            file://../libx11-${PV}/makekeys_crosscompile.patch"

EXTRA_OECONF += "--disable-xcms --with-xcb"
