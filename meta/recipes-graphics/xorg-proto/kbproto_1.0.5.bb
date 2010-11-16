require xorg-proto-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=7dd6ea99e2a83a552c02c80963623c38 \
                    file://XKBproto.h;beginline=2;endline=26;md5=5744eeff407aeb6e7a1346eebab486a2"

PR = "r0"
PE = "1"

DEPENDS += "gettext"

BBCLASSEXTEND = "native nativesdk"
