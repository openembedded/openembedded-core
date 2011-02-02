SUMMARY = "Generic PCI access library for X"

DESCRIPTION = "libpciaccess provides functionality for X to access the \
PCI bus and devices in a platform-independant way."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=aa44c26bc646c6c9d9619c73b94a6e31"

PR = "r0"

DEPENDS += "xproto virtual/libx11"

SRC_URI[md5sum] = "285e07976274572e1f1e68edee09b70a"
SRC_URI[sha256sum] = "f6b3c00e1c0aab0729563115d3d798e14c9210b4e10fccb484efe1c5eae85657"
