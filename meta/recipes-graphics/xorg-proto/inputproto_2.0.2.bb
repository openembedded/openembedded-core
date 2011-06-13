require xorg-proto-common.inc

SUMMARY = "XI: X Input extension headers"

DESCRIPTION = "This package provides the wire protocol for the X Input \
extension.  The extension supports input devices other then the core X \
keyboard and pointer."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=e562cc0f6587b961f032211d8160f31e \
                    file://XI2proto.h;endline=48;md5=1ac1581e61188da2885cc14ff49b20be"

PR = "r0"
PE = "1"

inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "07d54ae098ed4e6dce472f6ef3de05ce"
SRC_URI[sha256sum] = "64222a590ad4a62a3c8d57805379451769e3329cc5c8c5c1f1fc0d1529ebf005"

