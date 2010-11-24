DESCRIPTION = "X11 Session management library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d98446615b962372d6a46057170043fa"

DEPENDS += "libice xproto xtrans e2fsprogs"

PR = "r0"
PE = "1"

XORG_PN = "libSM"

BBCLASSEXTEND = "native"
