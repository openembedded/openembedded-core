DESCRIPTION = "A software library for accessing RDS data"
HOMEPAGE = "http://rdsd.berlios.de/"
SECTION = "libs"
PROVIDES = "librds"
LICENSE = "GPLv2"

PR = "r1"

SRC_URI = "http://download.berlios.de/rdsd/librds-${PV}.tar.gz"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
