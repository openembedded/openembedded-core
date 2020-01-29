SUMMARY = "Python 2 and 3 compatibility library"
HOMEPAGE = "https://pypi.python.org/pypi/six/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=43cfc9e4ac0e377acfb9b76f56b8415d"

SRC_URI[md5sum] = "21674588a57e649d1a6d977ec3122140"
SRC_URI[sha256sum] = "236bdbdce46e6e6a3d61a337c0f8b763ca1e8717c03b369e87a7ec7ce1319c0a"

inherit pypi

RDEPENDS_${PN} = "${PYTHON_PN}-io"

BBCLASSEXTEND = "native nativesdk"
