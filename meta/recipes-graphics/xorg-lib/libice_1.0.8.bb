SUMMARY = "ICE: Inter-Client Exchange library"

DESCRIPTION = "The Inter-Client Exchange (ICE) protocol provides a \
generic framework for building protocols on top of reliable, byte-stream \
transport connections. It provides basic mechanisms for setting up and \
shutting down connections, for performing authentication, for \
negotiating versions, and for reporting errors. "

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d162b1b3c6fa812da9d804dcf8584a93"

DEPENDS += "xproto xtrans"
PROVIDES = "ice"

PE = "1"

XORG_PN = "libICE"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "471b5ca9f5562ac0d6eac7a0bf650738"
SRC_URI[sha256sum] = "24a991284d02ff0c789bc8d11ad2e4dffe144cb70f24e28f9ce3e8b1ee08b71e"
