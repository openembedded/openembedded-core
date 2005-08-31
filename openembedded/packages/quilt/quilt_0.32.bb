include quilt.inc
PR = "r1"

SRC_URI = "http://savannah.nongnu.org/download/quilt/quilt-${PV}.tar.gz \
	   file://install.patch;patch=1 \
	   file://nostrip.patch;patch=1"
S = "${WORKDIR}/quilt-${PV}"

inherit autotools gettext

include quilt-package.inc
