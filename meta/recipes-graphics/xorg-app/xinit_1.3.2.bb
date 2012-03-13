require xorg-app-common.inc

SUMMARY = "X Window System initializer"

DESCRIPTION = "The xinit program is used to start the X Window System \
server and a first client program on systems that cannot start X \
directly from /etc/init or in environments that use multiple window \
systems. When this first client exits, xinit will kill the X server and \
then terminate."

LIC_FILES_CHKSUM = "file://COPYING;md5=0d4b5eef75f1584ccbdc5e4a34314407"

PR = "r0"
PE = "1"

SRC_URI[md5sum] = "9c0943cbd83e489ad1b05221b97efd44"
SRC_URI[sha256sum] = "a1867fdaa83f68750b12ba4305c3c62f5992d0f52cfeb98e96c27a8e690e0235"

EXTRA_OECONF = "ac_cv_path_MCOOKIE=${bindir}/mcookie"

RDEPENDS_${PN} += "util-linux-mcookie"

FILES_${PN} += "${libdir}X11/xinit"
