SUMMARY = "A library for property-based testing"
HOMEPAGE = "https://github.com/HypothesisWorks/hypothesis/tree/master/hypothesis-python"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4ee62c16ebd0f4f99d906f36b7de8c3c"

PYPI_PACKAGE = "hypothesis"

inherit pypi setuptools3

SRC_URI[sha256sum] = "5e25a0be23b1ff81ecf07e87d82e2e39a79c83c1458178af202ffe4b0183bcca"

RDEPENDS_${PN} += "python3-attrs python3-core python3-sortedcontainers"

BBCLASSEXTEND = "native nativesdk"
