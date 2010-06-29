DESCRIPTION = "X11 miscellaneous extension library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=c70692f0f710dda89d6cfcc412d8a1aa"

DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"

PR = "r0"
PE = "1"

XORG_PN = "libXext"

BBCLASSEXTEND = "nativesdk"
