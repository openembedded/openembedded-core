DESCRIPTION = "iniparse is a INI parser for Python"
HOMEPAGE = "http://code.google.com/p/iniparse/"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://iniparse.googlecode.com/files/iniparse-${PV}.tar.gz"
S = "${WORKDIR}/iniparse-${PV}"

inherit distutils
