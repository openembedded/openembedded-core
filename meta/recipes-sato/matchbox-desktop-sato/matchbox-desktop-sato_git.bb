DESCRIPTION = "Sato desktop folders"
HOMEPAGE = "http://matchbox-project.org"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"

SECTION = "x11"
DEPENDS = ""
CONFLICTS = "matchbox-common"

SRCREV = "810b0b08eb79e4685202da2ec347b990bb467e07"
PV = "0.1+git${SRCPV}"
PR = "r0"

SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}"
