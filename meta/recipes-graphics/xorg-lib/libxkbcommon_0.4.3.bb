SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c0b824e72a22f9d2c40b9c93b1f0ddc"
LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "26c57ff21438ed45de2a4ca609177db9"
SRC_URI[sha256sum] = "9a52d5d0419e76c49c0ece86208c205ffacb1cf0ff8ffbaba98d3d4dd40c4e41"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"

PACKAGECONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,libxcb xkeyboard-config,"
