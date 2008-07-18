DESCRIPTION = "OPKG Package Manager Utilities"
SECTION = "base"
PRIORITY = "optional"
LICENSE = "GPL"
RDEPENDS = "python"
PR = "r2"

SRC_URI = "svn://svn.openmoko.org/trunk/src/host/;module=opkg-utils;proto=http"

S = "${WORKDIR}/opkg-utils"

inherit autotools

S = "${WORKDIR}/opkg-utils"

do_stage () {
	autotools_stage_all
}
