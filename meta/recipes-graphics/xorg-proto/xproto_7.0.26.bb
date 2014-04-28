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

SRC_URI[md5sum] = "4dc2464bfeade23dab5de38da0f6b1b5"
SRC_URI[sha256sum] = "636162c1759805a5a0114a369dffdeccb8af8c859ef6e1445f26a4e6e046514f"

