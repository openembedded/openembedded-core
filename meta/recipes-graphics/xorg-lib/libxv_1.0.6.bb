SUMMARY = "Xv: X Video extension library"

DESCRIPTION = "libXv provides an X Window System client interface to the \
X Video extension to the X protocol. The X Video extension allows for \
accelerated drawing of videos.  Hardware adaptors are exposed to \
clients, which may draw in a number of colourspaces, including YUV."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=916ffd3d79baef9fb56d2c2af28f93f2"

DEPENDS += "libxext videoproto"

PR = "r0"

XORG_PN = "libXv"

SRC_URI[md5sum] = "e292445a64b63e918bbc8b6aae6391dd"
SRC_URI[sha256sum] = "e20f8e594bb0f44f3fbd25996945730391d72acbe5eaac760429fd6579caf5ee"
