require xorg-lib-common.inc

DESCRIPTION = "network API translation layer to insulate X applications and \
libraries from OS network vageries."
PE = "1"
PR = "r2"

SRC_URI += "file://fix-missing-includepath.patch;patch=1"

RDEPENDS_${PN}-dev = ""

XORG_PN = "xtrans"
