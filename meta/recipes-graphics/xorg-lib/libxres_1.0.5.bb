DESCRIPTION = "X11 Resource extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=8c89441a8df261bdc56587465e13c7fa"

DEPENDS += "libxext resourceproto"

PR = "r0"
PE = "1"

XORG_PN = "libXres"

SRC_URI[md5sum] = "d08f0b6df3f96c051637d37009f4e55a"
SRC_URI[sha256sum] = "a00b0f464bc0c038db5614513b0e33475db22a7b3cd41e4e56a6c661a518a059"
