require xorg-lib-common.inc

DESCRIPTION = "network API translation layer to insulate X applications and \
libraries from OS network vageries."
PE = "1"
PR = "r3"

RDEPENDS_${PN}-dev = ""

XORG_PN = "xtrans"
