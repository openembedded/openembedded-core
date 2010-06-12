require xorg-proto-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=f826d99765196352e6122a406cf0d024 \
                    file://renderproto.h;beginline=4;endline=24;md5=3e5e2851dad240b0a3a27c4776b4fd1f"

CONFLICTS = "renderext"
PR = "r1"
PE = "1"

BBCLASSEXTEND = "nativesdk"
