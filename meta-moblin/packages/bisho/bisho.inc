DESCRIPTION = "Web Services Settings"
SRC_URI = "http://download.moblin.org/sources/bisho/0.17/bisho-0.17.1.tar.bz2"
LICENSE = "GPLv2"

DEPENDS = "gtk+ mojito nbtk"

inherit autotools_stage

FILES_${PN} += "${datadir}/icons/"
