#This is a development snapshot, so lets hint OE to use the releases
DEFAULT_PREFERENCE = "-1"

require cairo.inc

SRC_URI = "http://cairographics.org/releases/cairo-${PV}.tar.gz \
           file://cairo-surface-cache-3.patch;patch=1"

PR = "r0"
