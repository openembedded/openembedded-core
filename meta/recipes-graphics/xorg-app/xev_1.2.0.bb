require xorg-app-common.inc
LIC_FILES_CHKSUM = "file://xev.c;beginline=1;endline=35;md5=2f39a0038b59269c1e62efae64cfd056"
DESCRIPTION = "X Event Viewer"
LICENSE = "MIT"
PE = "1"

DEPENDS += "libxrandr xproto"

SRC_URI += "file://diet-x11.patch"

SRC_URI[md5sum] = "2727c72f3eba0c23f8f6b2e618d195a2"
SRC_URI[sha256sum] = "3786a77dab17741d508d5d117a0ff33bb6eabf93e0935388b5f920bfcf2fb38f"
