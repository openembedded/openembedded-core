DESCRIPTION = "X11 font encoding library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=96254c20ab81c63e65b26f0dbcd4a1c1"

DEPENDS += "zlib xproto"
PR = "r0"
PE = "1"

BBCLASSEXTEND = "native"
