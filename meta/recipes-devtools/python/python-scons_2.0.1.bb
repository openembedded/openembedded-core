DESCRIPTION = "A Software Construction Tool"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=8481211ebbeaed9cdc7ad5a3b0c98aaf"
SRCNAME = "scons"

PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/scons/scons-${PV}.tar.gz"

SRC_URI[md5sum] = "beca648b894cdbf85383fffc79516d18"
SRC_URI[sha256sum] = "0a8151da41c4a26c776c84f44f747ce03e093d43be3e83b38c14a76ab3256762"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils
