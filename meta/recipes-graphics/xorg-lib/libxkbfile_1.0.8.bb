SUMMARY = "XKB: X Keyboard File manipulation library"

DESCRIPTION = "libxkbfile provides an interface to read and manipulate \
description files for XKB, the X11 keyboard configuration extension."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=8be7367f7e5d605a426f76bb37d4d61f"

DEPENDS += "virtual/libx11 kbproto"

PE = "1"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "19e6533ae64abba0773816a23f2b9507"
SRC_URI[sha256sum] = "8aa94e19c537c43558f30906650cea6e15fa012591445d9f927658c3b32a8f3a"
