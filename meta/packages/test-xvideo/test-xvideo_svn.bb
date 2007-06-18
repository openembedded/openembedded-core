DESCRIPTION = "Simple XVideo test application"
LICENSE = "GPL"
DEPENDS = "libx11 libxv"

PR = "r0"

SRC_URI="svn://svn.o-hand.com/repos/misc/trunk;module=${PN};srcdate=20070619;proto=http"

PV = "svn${SRCDATE}"

S = "${WORKDIR}/test-xvideo"

inherit module-base autotools

do_stage () {
        autotools_stage_all
}

