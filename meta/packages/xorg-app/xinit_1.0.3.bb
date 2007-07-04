require xorg-app-common.inc

DESCRIPTION = "X Window System initializer"
PR = "r1"
PE = "1"

FILES_${PN} += "${libdir}X11/xinit"
