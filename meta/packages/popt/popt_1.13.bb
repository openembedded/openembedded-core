DESCRIPTION = "The popt library for parsing command line options."
LICENSE = "MIT"
SECTION = "libs"
DEPENDS = "gettext-native"

SRC_URI = "http://rpm5.org/files/popt/popt-${PV}.tar.gz"

inherit autotools

do_stage () {
	 autotools_stage_all
}
