DESCRIPTION = "Simple XVideo test application"
LICENSE = "GPL"
DEPENDS = "libx11 libxv"

PR = "r1"
PV = "svn${SRCDATE}"

SRC_URI="svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

S = "${WORKDIR}/test-xvideo"

inherit module-base autotools

do_stage () {
        autotools_stage_all
}

