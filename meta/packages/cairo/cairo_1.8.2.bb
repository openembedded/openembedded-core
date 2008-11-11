require cairo.inc

PR = "r1"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
           file://hardcoded_libtool.patch;patch=1"
