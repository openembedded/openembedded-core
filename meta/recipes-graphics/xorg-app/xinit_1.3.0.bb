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

EXTRA_OECONF = "ac_cv_path_MCOOKIE=${bindir}/mcookie"

RDEPENDS_${PN} += "util-linux-mcookie"

FILES_${PN} += "${libdir}X11/xinit"

SRC_URI[md5sum] = "bc4e8b7d1919597cc37a0d24aa149dda"
SRC_URI[sha256sum] = "ba76e36e1a42a7cf76505b7e6fc4777f5d14f45ddff74341abfb7dd10d5fe04c"
