SUMMARY = "XRes: X Resource extension library"

DESCRIPTION = "libXRes provides an X Window System client interface to \
the Resource extension to the X protocol.  The Resource extension allows \
for X clients to see and monitor the X resource usage of various clients \
(pixmaps, et al)."

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=8c89441a8df261bdc56587465e13c7fa"

DEPENDS += "libxext resourceproto"

PR = "r0"
PE = "1"

XORG_PN = "libXres"

SRC_URI[md5sum] = "80d0c6d8522fa7a645e4f522e9a9cd20"
SRC_URI[sha256sum] = "ff8661c925e8b182f98ae98f02bbd93c55259ef7f34a92c1a126b6074ebde890"
