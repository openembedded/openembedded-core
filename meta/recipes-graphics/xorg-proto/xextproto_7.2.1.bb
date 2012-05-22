require xorg-proto-common.inc

SUMMARY = "XExt: X Extension headers"

DESCRIPTION = "This package provides the wire protocol for several X \
extensions.  These protocol extensions include DOUBLE-BUFFER, DPMS, \
Extended-Visual-Information, LBX, MIT_SHM, MIT_SUNDRY-NONSTANDARD, \
Multi-Buffering, SECURITY, SHAPE, SYNC, TOG-CUP, XC-APPGROUP, XC-MISC, \
XTEST.  In addition a small set of utility functions are also \
available."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=86f273291759d0ba2a22585cd1c06c53"

PR = "r0"
PE = "1"

inherit gettext

EXTRA_OECONF_append = " --enable-specs=no"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "eaac343af094e6b608cf15cfba0f77c5"
SRC_URI[sha256sum] = "7c53b105407ef3b2eb180a361bd672c1814524a600166a0a7dbbe76b97556d1a"
