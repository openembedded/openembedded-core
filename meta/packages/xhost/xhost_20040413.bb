FIXEDSRCDATE = "${@bb.data.getVar('FILE', d, 1).split('_')[-1].split('.')[0]}"
LICENSE = "MIT"
PV = "0.0+cvs${FIXEDSRCDATE}"
DEPENDS = "libx11 libxext libxmu"
DESCRIPTION = "server access control program for X"
MAINTAINER = "Rene Wagner <rw@handhelds.org>"
SECTION = "x11/base"
PR = "r2"

SRC_URI = "${FREEDESKTOP_CVS}/xorg;module=xc/programs/xhost;date=${FIXEDSRCDATE} \
           file://autofoo.patch;patch=1"
S = "${WORKDIR}/xhost"

inherit autotools pkgconfig 
