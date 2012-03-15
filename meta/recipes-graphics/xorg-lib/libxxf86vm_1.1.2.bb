SUMMARY = "XFree86-VM: XFree86 video mode extension library"

DESCRIPTION = "libXxf86vm provides an interface to the \
XFree86-VidModeExtension extension, which allows client applications to \
get and set video mode timings in extensive detail.  It is used by the \
xvidtune program in particular."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=fa0b9c462d8f2f13eba26492d42ea63d"

DEPENDS += "libxext xf86vidmodeproto"

PR = "r0"
PE = "1"

XORG_PN = "libXxf86vm"

SRC_URI[md5sum] = "ffd93bcedd8b2b5aeabf184e7b91f326"
SRC_URI[sha256sum] = "a564172fb866b1b587bbccb7d041088931029845245e0d15c32ca7f1bb48fc84"
