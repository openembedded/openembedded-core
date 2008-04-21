require cairo.inc

PR = "r1"

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
           file://configure_fix.patch;patch=1 "
