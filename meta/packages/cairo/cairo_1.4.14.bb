require cairo.inc

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
           file://configure_fix.patch;patch=1 "

PR = "r1"