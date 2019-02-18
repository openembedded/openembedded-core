SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e525ed9809e1f8a07cf4bce8b09e8b87"
LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "8225a206e00c4146d2e06f5e688b28e7"
SRC_URI[sha256sum] = "b855c629849a97ab9835a4ad99d6b749a636f70d38a03f070c6ef72024825540"

UPSTREAM_CHECK_URI = "http://xkbcommon.org/"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"

PACKAGECONFIG ?= "${@bb.utils.filter('DISTRO_FEATURES', 'x11', d)}"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,libxcb xkeyboard-config,"
