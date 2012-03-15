require xorg-lib-common.inc

SUMMARY = "XTest: X Test extension library"

DESCRIPTION = "This extension is a minimal set of client and server \
extensions required to completely test the X11 server with no user \
intervention."

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=cba677ca25c42ad29ee8ff351b50ece8 \
                    file://src/XTest.c;beginline=2;endline=32;md5=b1c8c9dff842b4d5b89ca5fa32c40e99"

DEPENDS += "libxext recordproto inputproto libxi"
PROVIDES = "xtst"
PR = "r0"
PE = "1"

XORG_PN = "libXtst"

SRC_URI[md5sum] = "e8abc5c00c666f551cf26aa53819d592"
SRC_URI[sha256sum] = "7eea3e66e392aca3f9dad6238198753c28e1c32fa4903cbb7739607a2504e5e0"
