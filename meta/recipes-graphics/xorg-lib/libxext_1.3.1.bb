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
LIC_FILES_CHKSUM = "file://COPYING;md5=f69eb72f85ce548e12791d049d81bc52"

DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"

PR = "r0"
PE = "1"

XORG_PN = "libXext"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "71251a22bc47068d60a95f50ed2ec3cf"
SRC_URI[sha256sum] = "56229c617eb7bfd6dec40d2805bc4dfb883dfe80f130d99b9a2beb632165e859"
