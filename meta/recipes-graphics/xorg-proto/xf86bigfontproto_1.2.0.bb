require xorg-proto-common.inc

SUMMARY = "XFree86-BIGFONT: XFree86 Big Font extension headers"

DESCRIPTION = "This package provides the wire protocol for the XFree86 \
Big Font extension.  This extension makes larger font requests \
possible."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=e01e66e4b317088cf869bc98e6af4fb6"

PR = "r1"
PE = "1"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "120e226ede5a4687b25dd357cc9b8efe"
SRC_URI[sha256sum] = "ba9220e2c4475f5ed2ddaa7287426b30089e4d29bd58d35fad57ba5ea43e1648"
