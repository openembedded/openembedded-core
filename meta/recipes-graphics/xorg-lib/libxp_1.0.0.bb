require xorg-lib-common.inc

DESCRIPTION = "X Printing Extension (Xprint) client library"

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=329e54eb6793b3d4830a4f6f1ca16e3f \
                    file://src/XpPage.c;beginline=2;endline=37;md5=886b68ac3721003d54abfbd82bafc5ad"

DEPENDS += "libxext libxau printproto"
PR = "r1"
PE = "1"

XORG_PN = "libXp"

CFLAGS_append += " -I ${S}/include/X11/XprintUtil -I ${S}/include/X11/extensions"

SRC_URI[md5sum] = "0f4ac39108c1ae8c443cdfac259b58fa"
SRC_URI[sha256sum] = "7e64b1550ce85b05762e960459ac676a0406c786756b200ff29c57f84bce9cae"
