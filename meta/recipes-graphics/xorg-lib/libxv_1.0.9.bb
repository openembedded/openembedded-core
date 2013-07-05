SUMMARY = "Xv: X Video extension library"

DESCRIPTION = "libXv provides an X Window System client interface to the \
X Video extension to the X protocol. The X Video extension allows for \
accelerated drawing of videos.  Hardware adaptors are exposed to \
clients, which may draw in a number of colourspaces, including YUV."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=827da9afab1f727f2a66574629e0f39c"

DEPENDS += "libxext videoproto"

XORG_PN = "libXv"

SRC_URI[md5sum] = "3f642e1d868a2289769b983cf346e7bb"
SRC_URI[sha256sum] = "a874dbf864d0271bbe795af67ef5b3f20096c92fc11eacbf0d2af00e32bc5b4b"
