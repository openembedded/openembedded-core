require xorg-lib-common.inc

SRC_URI += "file://abstract_socket_fix.patch;patch=1"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/xtrans"

DESCRIPTION = "network API translation layer to insulate X applications and \
libraries from OS network vageries."
PE = "1"
PR = "r4"

RDEPENDS_${PN}-dev = ""

XORG_PN = "xtrans"
