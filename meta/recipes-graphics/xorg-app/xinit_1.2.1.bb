require xorg-app-common.inc

DESCRIPTION = "X Window System initializer"
LIC_FILES_CHKSUM = "file://COPYING;md5=0d4b5eef75f1584ccbdc5e4a34314407"
PR = "r0"
PE = "1"

FILES_${PN} += "${libdir}X11/xinit"

SRC_URI[md5sum] = "56f2d202b3dc10fcd21931a67bb270f7"
SRC_URI[sha256sum] = "16bcc73ae81b6d80fd1a9419ea757f38870d1043d440b6d56fcaa79d4c6c9b07"
