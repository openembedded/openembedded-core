DESCRIPTION = "X Fixes extension library."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=90b90b60eb30f65fc9c2673d7cf59e24"

DEPENDS += "virtual/libx11 xproto fixesproto xextproto"

PR = "r0"
PE = "1"

XORG_PN = "libXfixes"

BBCLASSEXTEND = "nativesdk"
