DESCRIPTION = "Core utilities for Python packages"
HOMEPAGE = "https://github.com/pypa/packaging"
LICENSE = "Apache-2.0 | BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=faadaedca9251a90b205c9167578ce91"

SRC_URI[sha256sum] = "096d689d78ca690e4cd8a89568ba06d07ca097e3306a4381635073ca91479966"

SRC_URI += "file://8cb9dbf19e2b76ab025efc11208bd50e09e8223e.patch"

inherit pypi setuptools3

BBCLASSEXTEND = "native nativesdk"

DEPENDS += "${PYTHON_PN}-setuptools-native"
RDEPENDS:${PN} += "${PYTHON_PN}-pyparsing"
