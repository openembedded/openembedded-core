require xorg-lib-common.inc

DESCRIPTION = "X11 Rendering Extension client library"
LICENSE = "BSD-X"
DEPENDS += "virtual/libx11 renderproto xproto xdmcp"
PR = "r1"
PE = "1"

XORG_PN = "libXrender"
