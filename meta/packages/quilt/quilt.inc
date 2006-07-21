DESCRIPTION = "Tool to work with series of patches."
HOMEPAGE = "http://savannah.nongnu.org/projects/quilt/"
SECTION = "devel"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"
LICENSE = "GPL"

SRC_URI = "http://download.savannah.gnu.org/releases/quilt/quilt-${PV}.tar.gz \
	   file://install.patch;patch=1 \
	   file://nostrip.patch;patch=1"

SRC_URI_append_build-darwin = "	   file://non-gnu.patch;patch=1 "

S = "${WORKDIR}/quilt-${PV}"
