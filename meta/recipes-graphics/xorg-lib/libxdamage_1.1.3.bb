DESCRIPTION = "X11 damaged region extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=9fe101f30dd24134cf43146863241868"

DEPENDS += "damageproto libxfixes"
PROVIDES = "xdamage"

PR = "r0"
PE = "1"

XORG_PN = "libXdamage"
