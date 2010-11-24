DESCRIPTION = "X11 XFree86 video mode extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=0fe722b85f452ce13ca763f323ff5da8"

DEPENDS += "libxext xf86vidmodeproto"

PR = "r0"
PE = "1"

XORG_PN = "libXxf86vm"
