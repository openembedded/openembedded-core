require xorg-proto-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=ae2c93d7226d1ed33287c3924ca61816 \
                    file://xfixesproto.h;endline=41;md5=fa6bf4d92ae4dd0c1cac511105e541d2"

CONFLICTS = "fixesext"
PR = "r1"
PE = "1"

BBCLASSEXTEND = "nativesdk"
