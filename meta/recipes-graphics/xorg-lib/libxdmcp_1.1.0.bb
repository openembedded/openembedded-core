DESCRIPTION = "X Display Manager Control Protocol library"

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=d559fb26e129626022e052a5e6e0e123"

DEPENDS += "xproto"
PROVIDES = "xdmcp"

PR = "r0"
PE = "1"

DEPENDS += "gettext"

XORG_PN = "libXdmcp"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "762b6bbaff7b7d0831ddb4f072f939a5"
SRC_URI[sha256sum] = "b8a0e9a3192a3afddb56eb1d7adf933e423b401b2a492975d776dc0423c10072"
