DESCRIPTION = "X11 Resource extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=8c89441a8df261bdc56587465e13c7fa"

DEPENDS += "libxext resourceproto"

PR = "r0"
PE = "1"

XORG_PN = "libXres"
