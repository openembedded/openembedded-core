SUMMARY = "Xv: X Video extension library"

DESCRIPTION = "libXv provides an X Window System client interface to the \
X Video extension to the X protocol. The X Video extension allows for \
accelerated drawing of videos.  Hardware adaptors are exposed to \
clients, which may draw in a number of colourspaces, including YUV."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=827da9afab1f727f2a66574629e0f39c"

DEPENDS += "libxext videoproto"

PR = "r0"

XORG_PN = "libXv"

SRC_URI[md5sum] = "224dadca53d9c88f8a2b8945babcea70"
SRC_URI[sha256sum] = "4f00eb5347390909cea4e53a69425839d2a6a44e0e0613321d59e7e4aeaf73d7"
