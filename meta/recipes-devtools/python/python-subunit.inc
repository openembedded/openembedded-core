SUMMARY = "Python implementation of subunit test streaming protocol"
HOMEPAGE = "https://pypi.python.org/pypi/python-subunit/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://README;md5=e5b524e1b2c67c88fc64439ee4a850aa"

PYPI_PACKAGE = "python-subunit"

SRC_URI[md5sum] = "d2c09c93346077ced675c9f718e6a0f1"
SRC_URI[sha256sum] = "d9a7606e9610828d68c1d2f0f5abbb421e34e518b8f4882c8b2e08176281bf88"

inherit pypi

BBCLASSEXTEND = "nativesdk"
