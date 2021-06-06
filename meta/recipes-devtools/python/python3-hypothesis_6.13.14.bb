SUMMARY = "A library for property-based testing"
HOMEPAGE = "https://github.com/HypothesisWorks/hypothesis/tree/master/hypothesis-python"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4ee62c16ebd0f4f99d906f36b7de8c3c"

PYPI_PACKAGE = "hypothesis"

inherit pypi setuptools3

SRC_URI[sha256sum] = "36ef2d58f600be2973f694f45a55a5502de705d7594f9cf841276aec9082c414"

RDEPENDS_${PN} += "python3-attrs python3-core python3-sortedcontainers python3-unittest"

BBCLASSEXTEND = "native nativesdk"
