DESCRIPTION = "The popt library for parsing command line options."
LICENSE = "MIT"
SECTION = "libs"
DEPENDS = "gettext"

SRC_URI = "http://rpm5.org/files/popt/popt-${PV}.tar.gz"

inherit autotools_stage

BBCLASSEXTEND = "native"