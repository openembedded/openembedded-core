SECTION = "console/utils"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/desktop-file-utils"
DESCRIPTION = "command line utilities to work with *.desktop files"
LICENSE = "GPL"

DEPENDS = "glib-2.0-native"

SRC_URI = "http://freedesktop.org/software/desktop-file-utils/releases/desktop-file-utils-${PV}.tar.gz"

inherit autotools native

S = "${WORKDIR}/desktop-file-utils-${PV}"
