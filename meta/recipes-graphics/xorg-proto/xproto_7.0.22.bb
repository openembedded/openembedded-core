require xorg-proto-common.inc

SUMMARY = "Xlib: C Language X interface headers"

DESCRIPTION = "This package provides the basic headers for the X Window \
System."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=b9e051107d5628966739a0b2e9b32676"

PR = "r0"
PE = "1"

EXTRA_OECONF_append = " --enable-specs=no"
BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "da0b0eb2f432b7cc1d665b05422a0457"
SRC_URI[sha256sum] = "ad8397dd2a3de7249d2f3fb3a49444fef71483d43681285936c11911663817a8"

