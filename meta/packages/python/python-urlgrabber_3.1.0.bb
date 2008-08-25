DESCRIPTION = "urlgrabber is a pure python package that drastically simplifies the fetching of files."
HOMEPAGE = "http://linux.duke.edu/projects/urlgrabber/"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://linux.duke.edu/projects/urlgrabber/download/urlgrabber-${PV}.tar.gz"
S = "${WORKDIR}/urlgrabber-${PV}"

inherit distutils
