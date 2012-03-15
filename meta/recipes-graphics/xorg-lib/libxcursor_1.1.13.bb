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

SRC_URI[md5sum] = "52efa81b7f26c8eda13510a2fba98eea"
SRC_URI[sha256sum] = "f78827de4a1b7ce8cceca24a9ab9d1b1d2f6a61362f505166ffc19b07c0bad8f"
