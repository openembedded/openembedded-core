require xorg-app-common.inc

DESCRIPTION = "X Window System initializer"
LIC_FILES_CHKSUM = "file://COPYING;md5=0d4b5eef75f1584ccbdc5e4a34314407"
PR = "r0"
PE = "1"

FILES_${PN} += "${libdir}X11/xinit"

SRC_URI[md5sum] = "bc4e8b7d1919597cc37a0d24aa149dda"
SRC_URI[sha256sum] = "ba76e36e1a42a7cf76505b7e6fc4777f5d14f45ddff74341abfb7dd10d5fe04c"
