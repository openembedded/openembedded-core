SUMMARY = "Generic PCI access library for X"

DESCRIPTION = "libpciaccess provides functionality for X to access the \
PCI bus and devices in a platform-independant way."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=2c765efe1e0722c1badff5e54429b102"

PR = "r0"

DEPENDS += "xproto virtual/libx11"

SRC_URI[md5sum] = "d6363ee9f4df79f6fc47cba7c67b0d35"
SRC_URI[sha256sum] = "cc47d7f0e48cf4eed972916b536fdc97788d7521915e3ae1cc92d540776d7344"
