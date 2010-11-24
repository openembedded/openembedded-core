DESCRIPTION = "X11 Rendering Extension client library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=0db75cc842842b36f097fdae571b4b70"

DEPENDS += "virtual/libx11 renderproto xproto xdmcp"

PR = "r0"
PE = "1"

XORG_PN = "libXrender"

BBCLASSEXTEND = "nativesdk"
