require xorg-lib-common.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=fe7cbb8cc97683303f7814685cc47305"
DESCRIPTION = "X11 event trapping extension library"
DEPENDS += "libxt trapproto libxext"
PR = "r1"
PE = "1"

XORG_PN = "libXTrap"

SRC_URI[md5sum] = "1e2d966b5b2b89910e418bb0f78e10de"
SRC_URI[sha256sum] = "cfd12ce675bd5cdeac3720582741fe81a99343bef475d440cb34a7f2cdfe34ba"
