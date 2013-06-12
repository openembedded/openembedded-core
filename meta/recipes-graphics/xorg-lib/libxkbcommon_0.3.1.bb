SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"
LIC_FILES_CHKSUM = "file://COPYING;md5=9c0b824e72a22f9d2c40b9c93b1f0ddc"
LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "7287ea51df79c0f80e92b970a30b95e9"
SRC_URI[sha256sum] = "9c973581bba0c883a301fa6474d9c3e4f3a06c34e4ae4f1f4e113692cb18b38e"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"
