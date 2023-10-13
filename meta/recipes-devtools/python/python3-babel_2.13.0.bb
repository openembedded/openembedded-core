SUMMARY = "A collection of tools for internationalizing Python applications"
HOMEPAGE = "http://babel.edgewall.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0f97d9a63e91407b4c0d01efde91cfc0"

SRC_URI[sha256sum] = "04c3e2d28d2b7681644508f836be388ae49e0cfe91465095340395b60d00f210"

PYPI_PACKAGE = "Babel"

inherit pypi setuptools3

CLEANBROKEN = "1"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-codecs \
    ${PYTHON_PN}-difflib \
    ${PYTHON_PN}-distutils \
    ${PYTHON_PN}-netserver \
    ${PYTHON_PN}-numbers \
    ${PYTHON_PN}-pickle \
    ${PYTHON_PN}-pytz \
    ${PYTHON_PN}-shell \
    ${PYTHON_PN}-threading \
"

BBCLASSEXTEND = "native nativesdk"
