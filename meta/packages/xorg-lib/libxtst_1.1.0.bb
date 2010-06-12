require xorg-lib-common.inc

DESCRIPTION = "X Test Extension: client side library"

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=cba677ca25c42ad29ee8ff351b50ece8 \
                    file://src/XTest.c;beginline=2;endline=32;md5=1491d278aeb574e84c5235eb44cdac74"

DEPENDS += "libxext recordproto inputproto libxi"
PROVIDES = "xtst"
PR = "r2"
PE = "1"

XORG_PN = "libXtst"
