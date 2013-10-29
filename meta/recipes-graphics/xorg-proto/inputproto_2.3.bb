require xorg-proto-common.inc

SUMMARY = "XI: X Input extension headers"

DESCRIPTION = "This package provides the wire protocol for the X Input \
extension.  The extension supports input devices other then the core X \
keyboard and pointer."

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=e562cc0f6587b961f032211d8160f31e \
                    file://XI2proto.h;endline=48;md5=1ac1581e61188da2885cc14ff49b20be"

PE = "1"

inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "94db391e60044e140c9854203d080654"
SRC_URI[sha256sum] = "7d26b193f6699f8e9c1e28bf026844b7ecea33dd644402523471be109152c32f"

