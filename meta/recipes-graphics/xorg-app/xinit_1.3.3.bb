require xorg-app-common.inc

SUMMARY = "X Window System initializer"

DESCRIPTION = "The xinit program is used to start the X Window System \
server and a first client program on systems that cannot start X \
directly from /etc/init or in environments that use multiple window \
systems. When this first client exits, xinit will kill the X server and \
then terminate."

LIC_FILES_CHKSUM = "file://COPYING;md5=18f01e7b39807bebe2b8df101a039b68"

PE = "1"

SRC_URI[md5sum] = "3b8da0e6237aee9828cc809c647510a7"
SRC_URI[sha256sum] = "74b2878de6e3d5b5948f1562a77e7f53b4e10621e505ddb278050c7f3cae00af"

EXTRA_OECONF = "ac_cv_path_MCOOKIE=${bindir}/mcookie"

RDEPENDS_${PN} += "util-linux-mcookie"
