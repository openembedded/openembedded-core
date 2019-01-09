SUMMARY = "Python Build Reasonableness"
DESCRIPTION = "PBR is a library that injects some useful and sensible default behaviors into your setuptools run"
HOMEPAGE = "https://pypi.python.org/pypi/pbr"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

SRC_URI[md5sum] = "dbc94417a8ff0432d6244501a7f1bd49"
SRC_URI[sha256sum] = "f59d71442f9ece3dffc17bc36575768e1ee9967756e6b6535f0ee1f0054c3d68"

inherit pypi

RDEPENDS_${PN} += "${PYTHON_PN}-pip"

BBCLASSEXTEND = "native nativesdk"
