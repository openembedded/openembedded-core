SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e525ed9809e1f8a07cf4bce8b09e8b87"
LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "61ba550fc529ea4d6f9faa2cad62c95f"
SRC_URI[sha256sum] = "09351592312d67b438655f54da5b67853026662c4a57e6be4d225f04a9989798"

UPSTREAM_CHECK_URI = "http://xkbcommon.org/"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"

PACKAGECONFIG ?= "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)}"
PACKAGECONFIG[x11] = "--enable-x11,--disable-x11,libxcb xkeyboard-config,"
