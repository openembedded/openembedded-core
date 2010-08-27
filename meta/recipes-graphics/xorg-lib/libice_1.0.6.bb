DESCRIPTION = "X11 Inter-Client Exchange library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d162b1b3c6fa812da9d804dcf8584a93"

DEPENDS += "xproto xtrans"
PROVIDES = "ice"

PR = "r0"
PE = "1"

XORG_PN = "libICE"

BBCLASSEXTEND = "native"
