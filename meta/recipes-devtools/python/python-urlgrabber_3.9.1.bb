DESCRIPTION = "urlgrabber is a pure python package that drastically simplifies the fetching of files."

HOMEPAGE = "http://urlgrabber.baseurl.org/"
SECTION = "devel/python"
PRIORITY = "optional"
LICENSE = "GPL"
PR = "r1"

SRC_URI = "http://urlgrabber.baseurl.org/download/urlgrabber-${PV}.tar.gz \
           file://urlgrabber-HEAD.patch;patch=1 \
           file://urlgrabber-reset.patch;patch=1"
S = "${WORKDIR}/urlgrabber-${PV}"

DEPENDS = "python-pycurl"

inherit distutils