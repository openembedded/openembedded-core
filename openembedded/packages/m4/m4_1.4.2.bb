DESCRIPTION = "GNU m4 is an implementation of the traditional Unix macro \
processor."
LICENSE = "GPL"
MAINTAINER = "Chris Larson <kergoth@handhelds.org>"
SRC_URI = "${GNU_MIRROR}/m4/m4-${PV}.tar.gz \
	   file://make.patch;patch=1"
S = "${WORKDIR}/m4-${PV}"

inherit autotools

EXTRA_AUTORECONF = "--exclude=autopoint,aclocal"
EXTRA_OEMAKE += "'infodir=${infodir}'"
