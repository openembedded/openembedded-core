require xorg-lib-common.inc

SUMMARY = "XP: X Printing extension library"

DESCRIPTION = "libXp provides public APIs to allow client applications \
to render to non-display devices, making use of the X Print Service."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=9504a1264f5ddd4949254a57c0f8d6bb \
                    file://src/XpPage.c;beginline=2;endline=35;md5=2b7d3d2ba5505b19271cf31b6918997e"

DEPENDS += "libxext libxau printproto"
PR = "r1"
PE = "1"

XORG_PN = "libXp"

CFLAGS_append += " -I ${S}/include/X11/XprintUtil -I ${S}/include/X11/extensions"

SRC_URI += "file://fix-cast-error.patch"

SRC_URI[md5sum] = "7ae1d63748e79086bd51a633da1ff1a9"
SRC_URI[sha256sum] = "71d1f260005616d646b8c8788365f2b7d93911dac57bb53b65753d9f9e6443d2"
