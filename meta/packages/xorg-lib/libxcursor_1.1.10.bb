DESCRIPTION = "X cursor management library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=8902e6643f7bcd7793b23dcd5d8031a4"

DEPENDS += "libxrender libxfixes"

PR = "r0"
PE = "1"

XORG_PN = "libXcursor"
