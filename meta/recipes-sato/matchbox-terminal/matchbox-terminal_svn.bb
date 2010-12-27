DESCRIPTION = "Matchbox Terminal"
HOMEPAGE = "http://www.matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://main.c;endline=20;md5=96e39176d9e355639a0b8b1c7a840820"

DEPENDS = "gtk+ vte"
SECTION = "x11/utils"
PV = "0.0+svnr${SRCPV}"
PR = "r0"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig
