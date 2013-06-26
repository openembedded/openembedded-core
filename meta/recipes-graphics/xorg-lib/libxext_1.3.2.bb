SUMMARY = "XExt: X Extension library"

DESCRIPTION = "libXext provides an X Window System client interface to \
several extensions to the X protocol.  The supported protocol extensions \
are DOUBLE-BUFFER, DPMS, Extended-Visual-Information, LBX, MIT_SHM, \
MIT_SUNDRY-NONSTANDARD, Multi-Buffering, SECURITY, SHAPE, SYNC, TOG-CUP, \
XC-APPGROUP, XC-MISC, XTEST.  libXext also provides a small set of \
utility functions to aid authors of client APIs for X protocol \
extensions."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=879ce266785414bd1cbc3bc2f4d9d7c8"

DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"

PE = "1"

XORG_PN = "libXext"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "4376101e51bb2c6c44d9ab14344e85ad"
SRC_URI[sha256sum] = "f829075bc646cdc085fa25d98d5885d83b1759ceb355933127c257e8e50432e0"
