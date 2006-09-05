DESCRIPTION = "Package maintenance system for Debian."
LICENSE = "GPL"
SECTION = "base"
DEPENDS = "ncurses zlib"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"

SRC_URI = "${DEBIAN_MIRROR}/main/d/dpkg/dpkg_${PV}.tar.gz"
S = "${WORKDIR}/dpkg-${PV}"

PARALLEL_MAKE = ""
