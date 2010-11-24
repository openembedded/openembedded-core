DESCRIPTION = "X Fixes extension library."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=3c1ce42c334a6f5cccb0277556a053e0"

DEPENDS += "virtual/libx11 xproto fixesproto xextproto"

PR = "r0"
PE = "1"

XORG_PN = "libXfixes"

BBCLASSEXTEND = "nativesdk"
