require xorg-proto-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=e562cc0f6587b961f032211d8160f31e \
                    file://XI2proto.h;endline=48;md5=1ac1581e61188da2885cc14ff49b20be"

PR = "r0"
PE = "1"

inherit gettext

BBCLASSEXTEND = "native nativesdk"

SRC_URI[md5sum] = "da9bf9e5d174163f597d2d72757d9038"
SRC_URI[sha256sum] = "63663dd88df812738e0efdc52a18868c0756128f09748cbe89c8ec6d17124a44"

