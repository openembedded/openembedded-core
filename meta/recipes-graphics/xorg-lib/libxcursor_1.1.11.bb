SUMMARY = "Xcursor: X Cursor management library"

DESCRIPTION = "Xcursor is a simple library designed to help locate and \
load cursors. Cursors can be loaded from files or memory. A library of \
common cursors exists which map to the standard X cursor names. Cursors \
can exist in several sizes and the library automatically picks the best \
size."

require xorg-lib-common.inc

LICENSE = "MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=8902e6643f7bcd7793b23dcd5d8031a4"

DEPENDS += "libxrender libxfixes"

PR = "r0"
PE = "1"

XORG_PN = "libXcursor"

SRC_URI[md5sum] = "866ed46f7e0d85b8c0003cebbb78a4af"
SRC_URI[sha256sum] = "a06ef74579e2c06f9490e682b8e7ac915cb5280ee47bb071a2b850637a2bf6fe"
