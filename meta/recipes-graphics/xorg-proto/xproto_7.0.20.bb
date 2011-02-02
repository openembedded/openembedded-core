require xorg-proto-common.inc

SUMMARY = "Xlib: C Language X interface headers"

DESCRIPTION = "This package provides the basic headers for the X Window \
System."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=93ae8952e7d02d855516fbf2efb9a0d2"

PR = "r0"
PE = "1"

EXTRA_OECONF_append = "--enable-specs=no"
BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "65633168e5315c19defb4652cd3d83c1"
SRC_URI[sha256sum] = "3fe87fe47d9b795e60ba3715d71f90f9929d57e9048f1d2a6ebb558e5aee9e2a"

