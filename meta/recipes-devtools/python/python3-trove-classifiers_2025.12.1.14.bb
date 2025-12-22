SUMMARY = "Canonical source for classifiers on PyPI (pypi.org)."
HOMEPAGE = "https://github.com/pypa/trove-classifiers"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

SRC_URI[sha256sum] = "a74f0400524fc83620a9be74a07074b5cbe7594fd4d97fd4c2bfde625fdc1633"

PYPI_PACKAGE = "trove_classifiers"
UPSTREAM_CHECK_PYPI_PACKAGE = "${PYPI_PACKAGE}"

inherit pypi python_setuptools_build_meta ptest-python-pytest

DEPENDS += " python3-calver-native"

BBCLASSEXTEND = "native nativesdk"
