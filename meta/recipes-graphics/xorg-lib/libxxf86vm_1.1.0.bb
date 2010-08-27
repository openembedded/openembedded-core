DESCRIPTION = "X11 XFree86 video mode extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=de77b7cff98cf5d7a9a148069ebb4e1a"

DEPENDS += "libxext xf86vidmodeproto"

PR = "r0"
PE = "1"

XORG_PN = "libXxf86vm"
