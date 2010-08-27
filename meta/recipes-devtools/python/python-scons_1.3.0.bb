DESCRIPTION = "A Software Construction Tool"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "GPL"
SRCNAME = "scons"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/scons/scons-${PV}.tar.gz"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
