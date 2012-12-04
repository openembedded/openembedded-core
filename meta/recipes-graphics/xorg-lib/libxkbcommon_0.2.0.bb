SUMMARY = "Generic XKB keymap library"
DESCRIPTION = "libxkbcommon is a keymap compiler and support library which \
processes a reduced subset of keymaps as defined by the XKB specification."
HOMEPAGE = "http://www.xkbcommon.org"

LICENSE = "MIT & MIT-style"

DEPENDS = "util-macros flex-native bison-native"

SRC_URI = "http://xkbcommon.org/download/${BPN}-${PV}.tar.bz2"
SRC_URI[md5sum] = "2be3d4a255d02c7d46fc6a9486f21f6a"
SRC_URI[sha256sum] = "74eb0a121ca3998015fd687591426bb15c524645a72cf602831b2e729172fb42"

LIC_FILES_CHKSUM = "file://COPYING;md5=9c0b824e72a22f9d2c40b9c93b1f0ddc"

PR = "r1"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-docs"
