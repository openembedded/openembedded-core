DESCRIPTION = "Matchbox GTK+ theme configuration application."
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://appearance/mb-appearance.c;endline=25;md5=ea92333cf8a6802639d62d874c114a28"

DEPENDS = "gconf gtk+"
RDEPENDS_${PN} = "settings-daemon"

PV = "0.0+svnr${SRCPV}"
PR = "r1"

S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http \
        file://no-handed.patch;patch=1;pnum=0"

inherit autotools pkgconfig

