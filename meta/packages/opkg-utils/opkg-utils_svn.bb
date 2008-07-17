DESCRIPTION = "OPKG Package Manager Utilities"
SECTION = "base"
PRIORITY = "optional"
LICENSE = "GPL"
RDEPENDS = "python"
PR = "r1"

SRC_URI = "file://opkg-utils.tgz"

inherit autotools

S = "${WORKDIR}/opkg-utils"

do_stage () {
	autotools_stage_all
}
