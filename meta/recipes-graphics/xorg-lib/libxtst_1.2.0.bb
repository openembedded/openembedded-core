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
PR = "r2"
PE = "1"

XORG_PN = "libXtst"

SRC_URI[md5sum] = "7c592c72da6676f8b0aeec9133b81686"
SRC_URI[sha256sum] = "7a2e0912b521f6bd8c392189874ba4a3b8168b0bba4c2143e073de53d8e85408"
