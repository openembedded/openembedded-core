require xorg-lib-common.inc

SUMMARY = "XFree86-DGA: XFree86 Direct Graphics Access extension library"

DESCRIPTION = "libXxf86dga provides the XFree86-DGA extension, which \
allows direct graphics access to a framebuffer-like region, and also \
allows relative mouse reporting, et al.  It is mainly used by games and \
emulators for games."

LIC_FILES_CHKSUM = "file://COPYING;md5=abb99ac125f84f424a4278153988e32f"

DEPENDS += "libxext xf86dgaproto"

PE = "1"
PR = "r2"

SRC_URI += "file://libxxf86dga-1.1.3_fix_for_x32.patch"

SRC_URI[md5sum] = "b7f38465c46e7145782d37dbb9da8c09"
SRC_URI[sha256sum] = "551fa374dbef0f977de1f35d005fa9ffe92b7a87e82dbe62d6a4640f5b0b4994"

XORG_PN = "libXxf86dga"
