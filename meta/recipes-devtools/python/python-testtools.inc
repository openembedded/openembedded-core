SUMMARY = "Extensions to the Python standard library unit testing framework"
HOMEPAGE = "https://pypi.python.org/pypi/testtools/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e2c9d3e8ba7141c83bfef190e0b9379a"

inherit pypi

SRC_URI[md5sum] = "adef817b07ba24fd6d807fd41a4f1ef4"
SRC_URI[sha256sum] = "80f606607a6e4ce4d0e24e5b786562aa42c581906f3c070607a4265f3da65810"

DEPENDS += " \
    ${PYTHON_PN}-pbr \
    "

# Satisfy setup.py 'setup_requires'
DEPENDS += " \
    ${PYTHON_PN}-pbr-native \
    "

RDEPENDS_${PN} += "\
    ${PYTHON_PN}-extras \
    ${PYTHON_PN}-pbr \
    ${PYTHON_PN}-six \
    "

BBCLASSEXTEND = "nativesdk"
