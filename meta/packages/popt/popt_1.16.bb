DESCRIPTION = "The popt library for parsing command line options."
HOMEPAGE = "http://rpm5.org/"
SECTION = "libs"
DEPENDS = "gettext"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=cb0613c30af2a8249b8dcc67d3edb06d"
PR = "r0"

SRC_URI = "http://rpm5.org/files/popt/popt-${PV}.tar.gz"

inherit autotools

BBCLASSEXTEND = "native"
