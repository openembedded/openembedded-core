DESCRIPTION = "X cursor management library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=8902e6643f7bcd7793b23dcd5d8031a4"

DEPENDS += "libxrender libxfixes"

PR = "r0"
PE = "1"

XORG_PN = "libXcursor"

SRC_URI[md5sum] = "866ed46f7e0d85b8c0003cebbb78a4af"
SRC_URI[sha256sum] = "a06ef74579e2c06f9490e682b8e7ac915cb5280ee47bb071a2b850637a2bf6fe"
