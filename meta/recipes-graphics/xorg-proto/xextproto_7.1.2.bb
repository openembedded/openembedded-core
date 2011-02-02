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

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "263ae968b223c23b2986603d84e5c30e"
SRC_URI[sha256sum] = "f6f829e112c8eca7c2f10b2193e8d927b9b7722283d647cfd2aea09758159199"
