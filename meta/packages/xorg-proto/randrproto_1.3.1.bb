require xorg-proto-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=5fa3f85d9eefaa3a945071485be11343 \
                    file://randrproto.h;endline=30;md5=3885957c6048fdf3310ac8ba54ca2c3f"

CONFLICTS = "randrext"
PR = "r1"
PE = "1"

BBCLASSEXTEND = "nativesdk"
