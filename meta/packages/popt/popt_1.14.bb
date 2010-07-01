DESCRIPTION = "The popt library for parsing command line options."
HOMEPAGE = "http://rpm5.org/"
SECTION = "libs"
DEPENDS = "gettext"

LICENSE = "MIT"

SRC_URI = "http://rpm5.org/files/popt/popt-${PV}.tar.gz"

inherit autotools

BBCLASSEXTEND = "native"
