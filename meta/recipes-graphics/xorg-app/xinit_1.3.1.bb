require xorg-app-common.inc

SUMMARY = "X Window System initializer"

DESCRIPTION = "The xinit program is used to start the X Window System \
server and a first client program on systems that cannot start X \
directly from /etc/init or in environments that use multiple window \
systems. When this first client exits, xinit will kill the X server and \
then terminate."

LIC_FILES_CHKSUM = "file://COPYING;md5=0d4b5eef75f1584ccbdc5e4a34314407"

PR = "r2"
PE = "1"

SRC_URI[md5sum] = "ee234056d8a3dbf37b61b4bcb35b88e4"
SRC_URI[sha256sum] = "c7468dfae94bdb42785d79623c27156dc4bf379d8372992830482cb04d8439b0"

EXTRA_OECONF = "ac_cv_path_MCOOKIE=${bindir}/mcookie"

RDEPENDS_${PN} += "util-linux-mcookie"

FILES_${PN} += "${libdir}X11/xinit"
