require xorg-lib-common.inc

DESCRIPTION = "XFIXES Extension"
DEPENDS += " libxext xextproto xproto zlib"
PROVIDES = "lbxutil"
PE = "1"
PR = "r1"

SRC_URI += "file://mkg3states.patch;patch=1"

export CC_FOR_BUILD = "gcc"
