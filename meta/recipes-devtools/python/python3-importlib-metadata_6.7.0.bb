SUMMARY = "Read metadata from Python packages"
HOMEPAGE = "https://pypi.org/project/importlib-metadata/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "importlib_metadata"
UPSTREAM_CHECK_REGEX = "/importlib-metadata/(?P<pver>(\d+[\.\-_]*)+)/"

SRC_URI[sha256sum] = "1aaf550d4f73e5d6783e7acb77aec43d49da8017410afae93822cc9cca98c4d4"

S = "${WORKDIR}/importlib_metadata-${PV}"

DEPENDS += "${PYTHON_PN}-setuptools-scm-native ${PYTHON_PN}-toml-native"
RDEPENDS:${PN} += "${PYTHON_PN}-zipp ${PYTHON_PN}-pathlib2"
RDEPENDS:${PN}:append:class-target = " python3-misc"
RDEPENDS:${PN}:append:class-nativesdk = " python3-misc"

BBCLASSEXTEND = "native nativesdk"
