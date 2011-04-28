DESCRIPTION = "Sato desktop folders"
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

SECTION = "x11"
DEPENDS = ""
CONFLICTS = "matchbox-common"
SRCREV = "76"
PV = "0.0+svnr${SRCPV}"
PR = "r0"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=desktop-folders;proto=http"
S = "${WORKDIR}/desktop-folders"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}"
