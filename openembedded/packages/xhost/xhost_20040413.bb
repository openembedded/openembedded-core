FIXEDCVSDATE = "${@bb.data.getVar('FILE', d, 1).split('_')[-1].split('.')[0]}"
LICENSE = "MIT"
PV = "0.0cvs${FIXEDCVSDATE}"
DEPENDS = "x11 xext xmu"
DESCRIPTION = "server access control program for X"
MAINTAINER = "Rene Wagner <rw@handhelds.org>"
SECTION = "x11/base"
PR = "r2"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xorg;module=xc/programs/xhost;date=${FIXEDCVSDATE} \
           file://autofoo.patch;patch=1"
S = "${WORKDIR}/xhost"

inherit autotools pkgconfig 
