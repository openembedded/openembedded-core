require xorg-proto-common.inc

SUMMARY = "XKB: X Keyboard extension headers"

DESCRIPTION = "This package provides the wire protocol for the X \
Keyboard extension.  This extension is used to control options related \
to keyboard handling and layout."

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=7dd6ea99e2a83a552c02c80963623c38 \
                    file://XKBproto.h;beginline=1;endline=25;md5=5744eeff407aeb6e7a1346eebab486a2"

PE = "1"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "677ea8523eec6caca86121ad2dca0b71"
SRC_URI[sha256sum] = "037cac0aeb80c4fccf44bf736d791fccb2ff7fd34c558ef8f03ac60b61085479"
