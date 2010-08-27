require xorg-lib-common.inc

DESCRIPTION = "X11 Input extension library"

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=2aafbfe6127f6f03eb776283e6138cce \
                    file://src/XIGetDevFocus.c;endline=23;md5=cdfb0d435a33ec57ea0d1e8e395b729f"

DEPENDS += "libxext inputproto"
PE = "1"

XORG_PN = "libXi"

