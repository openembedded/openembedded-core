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

SRC_URI[md5sum] = "5e1ac203ccd3ce3e89755ed1fbe75b0b"
SRC_URI[sha256sum] = "5d664aeb641f8c867331a0c6b4574a5e7e420f00bf5fcefd874e8d003ea59010"
