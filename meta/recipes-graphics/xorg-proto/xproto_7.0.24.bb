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

SRC_URI[md5sum] = "9c0203c3bee4bac432ec504dc45712ed"
SRC_URI[sha256sum] = "3698a1c7e3b734bc1139a7eb694ed8c66188658d6b4dea3d567066fe4e88b2fc"

