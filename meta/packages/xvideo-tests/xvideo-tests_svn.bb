DESCRIPTION = "Simple XVideo test application"
LICENSE = "GPL"
DEPENDS = "libxv"
PV = "0.0+svnr${SRCREV}"
PR = "r0"

SRC_URI="svn://svn.o-hand.com/repos/misc/trunk;module=test-xvideo;proto=http"

S = "${WORKDIR}/test-xvideo"

inherit autotools
