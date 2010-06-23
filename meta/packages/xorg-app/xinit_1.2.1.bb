require xorg-app-common.inc

DESCRIPTION = "X Window System initializer"
LIC_FILES_CHKSUM = "file://COPYING;md5=0d4b5eef75f1584ccbdc5e4a34314407"
PR = "r0"
PE = "1"

FILES_${PN} += "${libdir}X11/xinit"
