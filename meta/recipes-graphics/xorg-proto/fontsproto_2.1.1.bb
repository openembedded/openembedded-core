require xorg-proto-common.inc

SUMMARY = "XFont: X Font rasterisation headers"

DESCRIPTION = "This package provides the wire protocol for the X Font \
rasterisation extensions.  These extensions are used to control \
server-side font configurations."

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=c3e48aa9ce868c8e90f0401db41c11a2 \
                    file://FSproto.h;endline=44;md5=d2e58e27095e5ea7d4ad456ccb91986c"

PR = "r0"
PE = "1"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "37102ffcaa73f77d700acd6f7a25d8f0"
SRC_URI[sha256sum] = "9c9abc81e2927e6b6ffc6eece1f3fbc7559f506a2848673a21e72c0ae4d639bc"
