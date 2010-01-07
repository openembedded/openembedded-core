DESCRIPTION = "Display or change block device settings"
LICENSE = "GPLv2"
DEPENDS = "glib-2.0"

SRC_URI = "${DEBIAN_MIRROR}/main/b/blktool/blktool_4.orig.tar.gz"

S = "${WORKDIR}/${PN}-${PV}.orig"

inherit autotools