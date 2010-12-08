DESCRIPTION = "X11 damaged region extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=9fe101f30dd24134cf43146863241868"

DEPENDS += "damageproto libxfixes"
PROVIDES = "xdamage"

PR = "r0"
PE = "1"

XORG_PN = "libXdamage"

SRC_URI[md5sum] = "44774e1a065158b52f1a0da5100cebec"
SRC_URI[sha256sum] = "bc6169c826d3cb17435ca84e1b479d65e4b51df1e48bbc3ec39a9cabf842c7a8"
