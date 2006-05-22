DESCRIPTION = "user preference utility for X"
LICENSE = "MIT"
MAINTAINER = "Florian Boor <florian.boor@kernelconcepts.de>"
FIXEDSRCDATE = "${@bb.data.getVar('FILE', d, 1).split('_')[-1].split('.')[0]}"
PV = "0.0+cvs${FIXEDSRCDATE}"
PR = "r1"

DEPENDS = "libx11 libxext xextensions libxmu"

CFLAGS += "-D_GNU_SOURCE"

SECTION = "x11/base"

SRC_URI = "${FREEDESKTOP_CVS}/xorg;module=xc/programs/xset;date=${FIXEDSRCDATE} \
           file://autofoo.patch;patch=1"
S = "${WORKDIR}/xset"

inherit autotools pkgconfig 
