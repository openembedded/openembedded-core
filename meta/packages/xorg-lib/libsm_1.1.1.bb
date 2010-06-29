DESCRIPTION = "X11 Session management library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=451a87de5b38d25ec6b96d548087934d"

DEPENDS += "libice xproto xtrans e2fsprogs"

PR = "r0"
PE = "1"

XORG_PN = "libSM"

BBCLASSEXTEND = "native"
