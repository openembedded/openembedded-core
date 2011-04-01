require xorg-proto-common.inc

SUMMARY = "XKB: X Keyboard extension headers"

DESCRIPTION = "This package provides the wire protocol for the X \
Keyboard extension.  This extension is used to control options related \
to keyboard handling and layout."

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=7dd6ea99e2a83a552c02c80963623c38 \
                    file://XKBproto.h;beginline=2;endline=26;md5=5744eeff407aeb6e7a1346eebab486a2"

PR = "r0"
PE = "1"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "e7edb59a3f54af15f749e8f3e314ee62"
SRC_URI[sha256sum] = "0eba4f525e1700798cc0585fe29556e4369fba6517c670866273ad104cf5f49d"
