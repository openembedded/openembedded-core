require xorg-proto-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=b9f79c119df756aeffcb89ec96716a9e \
                    file://XI2proto.h;endline=48;md5=1ac1581e61188da2885cc14ff49b20be"

PR = "r2"
PE = "1"

inherit gettext

BBCLASSEXTEND = "native nativesdk"
