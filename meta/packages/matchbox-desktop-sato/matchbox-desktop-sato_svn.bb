SECTION = "x11"
DESCRIPTION = "Sato desktop folders"
LICENSE = "LGPL"
DEPENDS = ""
CONFLICTS = "matchbox-common"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/sato/trunk;module=desktop-folders;proto=http"
S = "${WORKDIR}/desktop-folders"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}"
