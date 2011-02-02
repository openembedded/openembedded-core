SUMMARY = "XFixes: X Fixes extension library."

DESCRIPTION = "X applications have often needed to work around various \
shortcomings in the core X window system.  This extension is designed to \
provide the minimal server-side support necessary to eliminate problems \
caused by these workarounds."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=3c1ce42c334a6f5cccb0277556a053e0"

DEPENDS += "virtual/libx11 xproto fixesproto xextproto"

PR = "r0"
PE = "1"

XORG_PN = "libXfixes"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "1b4b8386bd5d1751b2c7177223ad4629"
SRC_URI[sha256sum] = "2e6cd020460e4ef5d5a1d9b5d21143e9f5e580036a79c7de26ae539d7bcb8d74"
