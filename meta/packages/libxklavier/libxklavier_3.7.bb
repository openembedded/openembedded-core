DESCRIPTION = "Utility library to make using XKB easier"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "iso-codes libxml2 glib-2.0 libxkbfile"
LICENSE = "LGPL"

SRC_URI = "http://www.mirrorservice.org/sites/ftp.sourceforge.net/pub/sourceforge/g/gs/gswitchit/libxklavier-${PV}.tar.gz \
           file://remove-include-dir.patch;patch=1"
inherit autotools

