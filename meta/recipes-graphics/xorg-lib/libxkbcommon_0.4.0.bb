SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c0b824e72a22f9d2c40b9c93b1f0ddc"
LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "bb782f1700e45b4f57c84ef513e3cf85"
SRC_URI[sha256sum] = "2dc018e830d1befaf4822615204138318da3fff0112f78f3a7c638fa617375b8"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"

PACKAGECONFIG ?= "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,libxcb"
