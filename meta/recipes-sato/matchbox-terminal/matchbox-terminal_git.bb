DESCRIPTION = "Matchbox Terminal"
HOMEPAGE = "http://www.matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://main.c;endline=20;md5=96e39176d9e355639a0b8b1c7a840820"

DEPENDS = "gtk+ vte"
SECTION = "x11/utils"
SRCREV = "3fc25cb811a8ed306de897edf9b930f4402f3852"
PV = "0.0+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
