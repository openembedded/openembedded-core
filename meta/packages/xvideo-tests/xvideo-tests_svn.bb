DESCRIPTION = "Simple XVideo test application"
LICENSE = "GPL"
DEPENDS = "virtual/libx11 libxv"

PV = "0.0+svn${SRCDATE}"
PR = "r6"

SRC_URI="svn://svn.o-hand.com/repos/misc/trunk;module=test-xvideo;proto=http"
#SRC_URI="file://xvideo-tests-0.0.1.tar.gz"

S = "${WORKDIR}/test-xvideo"
#S = "${WORKDIR}/xvideo-tests-0.0.1"

inherit autotools

do_stage () {
        autotools_stage_all
}

