DESCRIPTION = "Simple XVideo test application"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/test-xvideo.c;beginline=1;endline=20;md5=6ae3b4c3c2ff9e51dbbc35bb237afa00"
DEPENDS = "libxv"

SRCREV = "270"
PV = "0.0+svnr${SRCPV}"
PR = "r0"

SRC_URI="svn://svn.o-hand.com/repos/misc/trunk;module=test-xvideo;proto=http"

S = "${WORKDIR}/test-xvideo"

inherit autotools
