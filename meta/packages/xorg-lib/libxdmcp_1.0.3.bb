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
