SUMMARY = "Extensions to the Python standard library unit testing framework"
HOMEPAGE = "https://pypi.org/project/testtools/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e2c9d3e8ba7141c83bfef190e0b9379a"

inherit pypi

SRC_URI[sha256sum] = "eec326f4275e6a55e5540302d01a0ae0a9e48991d7aade38a5c8ed093c37993a"

DEPENDS += " \
    ${PYTHON_PN}-pbr \
    "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
    ${PYTHON_PN}-pbr-native \
    "

RDEPENDS:${PN} += "\
    ${PYTHON_PN}-doctest \
    ${PYTHON_PN}-extras \
    ${PYTHON_PN}-pbr \
    ${PYTHON_PN}-six \
    "

BBCLASSEXTEND = "nativesdk"
