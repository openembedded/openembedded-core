DESCRIPTION = "X11 damaged region extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=10c8c88d1faea6d7c3a82d54c2b8fd2b"

DEPENDS += "damageproto libxfixes"
PROVIDES = "xdamage"

PR = "r0"
PE = "1"

XORG_PN = "libXdamage"
