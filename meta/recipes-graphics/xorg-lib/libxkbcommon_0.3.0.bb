SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c0b824e72a22f9d2c40b9c93b1f0ddc"
LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "22a046100738f99b4cc0297aa210f4e4"
SRC_URI[sha256sum] = "866c0df88f806dff8fc859b6f082cf9f8b6c3b549f0a367541e12b1ca28a5d65"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"
