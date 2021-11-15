DESCRIPTION = "Read metadata from Python packages"
HOMEPAGE = "https://pypi.org/project/importlib-metadata/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e88ae122f3925d8bde8319060f2ddb8e"

inherit pypi setuptools3

PYPI_PACKAGE = "importlib_metadata"
UPSTREAM_CHECK_REGEX = "/importlib-metadata/(?P<pver>(\d+[\.\-_]*)+)/"

SRC_URI[sha256sum] = "75bdec14c397f528724c1bfd9709d660b33a4d2e77387a3358f20b848bb5e5fb"

S = "${WORKDIR}/importlib_metadata-${PV}"

DEPENDS += "${PYTHON_PN}-setuptools-scm-native ${PYTHON_PN}-toml-native"
RDEPENDS:${PN} += "${PYTHON_PN}-zipp ${PYTHON_PN}-pathlib2"
RDEPENDS:${PN}:append:class-target = " python3-misc"
RDEPENDS:${PN}:append:class-nativesdk = " python3-misc"

BBCLASSEXTEND = "native nativesdk"
