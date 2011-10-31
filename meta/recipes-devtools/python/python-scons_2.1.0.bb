DESCRIPTION = "A Software Construction Tool"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=ab8b65435c2e520ed18e67459f1f9bb9"
SRCNAME = "scons"

PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/scons/scons-${PV}.tar.gz"

SRC_URI[md5sum] = "47daf989e303a045b76c11236df719df"
SRC_URI[sha256sum] = "4139ed14f60dd2ebcd47c59984d14705636180eb27b3d1b2949489e514b1921d"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
