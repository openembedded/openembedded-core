DESCRIPTION = "GNOME XML Parser library"
SECTION = "libs"
PRIORITY = "optional"
LICENSE = "MIT"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxml2-${PV}.tar.gz \
           file://libxml-64bit.patch;patch=1"

inherit autotools pkgconfig binconfig

EXTRA_OECONF = "--without-python --without-debug --without-legacy --without-catalog --without-docbook --with-c14n"
