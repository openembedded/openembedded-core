DESCRIPTION = "A Sample Authorization Protocol for X"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=7908e342491198401321cec1956807ec"

DEPENDS += " xproto gettext"
PROVIDES = "xau"

PR = "r0"
PE = "1"

XORG_PN = "libXau"

BBCLASSEXTEND = "native nativesdk"
