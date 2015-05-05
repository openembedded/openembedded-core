require xorg-proto-common.inc

SUMMARY = "Xlib: C Language X interface headers"

DESCRIPTION = "This package provides the basic headers for the X Window \
System."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=b9e051107d5628966739a0b2e9b32676"

PE = "1"

SRC_URI += "file://xproto_fix_for_x32.patch"

EXTRA_OECONF_append = " --enable-specs=no"
BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "6c3345111a8044f730984988aacfa567"
SRC_URI[sha256sum] = "246f23a6b235c8008183deeb1dcd76d529a099f93600b648b399811b10ea110c"
