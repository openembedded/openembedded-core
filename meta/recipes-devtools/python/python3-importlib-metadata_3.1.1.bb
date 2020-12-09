DESCRIPTION = "Read metadata from Python packages"
HOMEPAGE = "https://pypi.org/project/importlib-metadata/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e88ae122f3925d8bde8319060f2ddb8e"

inherit pypi setuptools3

SRC_URI = "https://files.pythonhosted.org/packages/3f/a8/16dc098b0addd1c20719c18a86e985be851b3ec1e103e703297169bb22cc/importlib_metadata-3.1.1.tar.gz"
SRC_URI[sha256sum] = "b0c2d3b226157ae4517d9625decf63591461c66b3a808c2666d538946519d170"

S = "${WORKDIR}/importlib_metadata-${PV}"

DEPENDS += "${PYTHON_PN}-setuptools-scm-native"
RDEPENDS_${PN} += "${PYTHON_PN}-zipp ${PYTHON_PN}-pathlib2"
RDEPENDS_${PN}_append_class-target = " python3-misc"
RDEPENDS_${PN}_append_class-nativesdk = " python3-misc"

BBCLASSEXTEND = "native nativesdk"
