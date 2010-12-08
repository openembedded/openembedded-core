require xorg-proto-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=f826d99765196352e6122a406cf0d024 \
                    file://renderproto.h;beginline=4;endline=24;md5=3e5e2851dad240b0a3a27c4776b4fd1f"

CONFLICTS = "renderext"
PR = "r1"
PE = "1"

BBCLASSEXTEND = "nativesdk"

SRC_URI[md5sum] = "b160a9733fe91b666e74fca284333148"
SRC_URI[sha256sum] = "c4d1d6d9b0b6ed9a328a94890c171d534f62708f0982d071ccd443322bedffc2"
