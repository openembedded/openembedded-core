require xorg-lib-common.inc

export CC_FOR_BUILD = "gcc"

SRC_URI += "file://mkg3states.patch;patch=1"

DESCRIPTION = "XFIXES Extension"

DEPENDS += " xextproto xproto zlib"
PROVIDES = "lbxutil"
