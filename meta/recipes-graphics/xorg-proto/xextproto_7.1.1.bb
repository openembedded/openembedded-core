require xorg-proto-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=86f273291759d0ba2a22585cd1c06c53"

PR = "r0"
PE = "1"

inherit gettext

BBCLASSEXTEND = "native nativesdk"
