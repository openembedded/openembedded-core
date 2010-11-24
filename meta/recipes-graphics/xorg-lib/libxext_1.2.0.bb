DESCRIPTION = "X11 miscellaneous extension library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=0542b0321c1c9a7a20b23a1b9fa45f91"

DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"

PR = "r0"
PE = "1"

XORG_PN = "libXext"

BBCLASSEXTEND = "nativesdk"
