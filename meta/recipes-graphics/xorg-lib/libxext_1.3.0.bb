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
LIC_FILES_CHKSUM = "file://COPYING;md5=9b0eca8f3540b7e7f8b447154a20b05a"

DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"

PR = "r0"
PE = "1"

XORG_PN = "libXext"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "161d200b690ace818db1cc7537e70ba9"
SRC_URI[sha256sum] = "e9daeb400855b9836e328500cec356b2769033174fc1b2be0df4a80f031debc0"
