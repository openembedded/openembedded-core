DESCRIPTION = "Utility library to make using XKB easier"
SECTION = "x11/libs"
DEPENDS = "iso-codes libxml2 glib-2.0 libxkbfile"
LICENSE = "LGPL"
PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/gswitchit/libxklavier-${PV}.tar.bz2 \
           file://pkgconfigfix.patch;patch=1"

inherit autotools

