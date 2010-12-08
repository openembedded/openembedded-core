require xorg-lib-common.inc

DESCRIPTION = "X11 Xinerama extension library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=6f4f634d1643a2e638bba3fcd19c2536 \
                    file://src/Xinerama.c;beginline=2;endline=25;md5=fcef273bfb66339256411dd06ea79c02"

DEPENDS += "libxext xineramaproto"
PROVIDES = "xinerama"
PR = "r3"
PE = "1"

XORG_PN = "libXinerama"

SRC_URI[md5sum] = "ecd4839ad01f6f637c6fb5327207f89b"
SRC_URI[sha256sum] = "bbe2b4a0e8ccf01a40f02c429c8418bd1fe652fd2c6f05d487e1319599d6779f"
