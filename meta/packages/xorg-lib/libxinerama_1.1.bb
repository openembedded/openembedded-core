require xorg-lib-common.inc

DESCRIPTION = "X11 Xinerama extension library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=4641deddaa80fe7ca88e944e1fd94a94 \
                    file://src/Xinerama.c;beginline=2;endline=25;md5=098e0bc089368a988092b3cbda617a57"

DEPENDS += "libxext xineramaproto"
PROVIDES = "xinerama"
PR = "r3"
PE = "1"

XORG_PN = "libXinerama"
