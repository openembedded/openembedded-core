SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c0b824e72a22f9d2c40b9c93b1f0ddc"
LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "935cf416354bf05210de2e389484f7e8"
SRC_URI[sha256sum] = "cc378a47b01b00226ecf647b69e07df04091323846124b366cf835e5cdf5a88a"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"
