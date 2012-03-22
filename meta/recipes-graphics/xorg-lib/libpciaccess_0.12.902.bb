SUMMARY = "Generic PCI access library for X"

DESCRIPTION = "libpciaccess provides functionality for X to access the \
PCI bus and devices in a platform-independent way."

require xorg-lib-common.inc

SRC_URI += "file://fix_deletion_of_last_handle.patch"

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=de01cb89a769dc657d4c321c209ce4fc"

PR = "r0"

DEPENDS += "xproto virtual/libx11"

SRC_URI[md5sum] = "f1db198398a8a1143822acc230843e8c"
SRC_URI[sha256sum] = "0861d5bf68b598baa307e5c9b06dfd38ae03096b46e36b236106517bcd14b63a"
