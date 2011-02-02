require xorg-lib-common.inc

SUMMARY = "XFree86-DGA: XFree86 Direct Graphics Access extension library"

DESCRIPTION = "libXxf86dga provides the XFree86-DGA extension, which \
allows direct graphics access to a framebuffer-like region, and also \
allows relative mouse reporting, et al.  It is mainly used by games and \
emulators for games."

DEPENDS += "libxext xf86dgaproto"
PR = "r1"
PE = "1"

XORG_PN = "libXxf86dga"

LIC_FILES_CHKSUM = "file://COPYING;md5=abb99ac125f84f424a4278153988e32f"

SRC_URI[md5sum] = "bbd5fdf63d4c107c8cb710d4df2012b4"
SRC_URI[sha256sum] = "1ba652f562ce3fb3fef092ce5485eb7ef15b521124c901977b56d6f324605a06"
