SUMMARY = "A library for property-based testing"
HOMEPAGE = "https://github.com/HypothesisWorks/hypothesis/tree/master/hypothesis-python"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4ee62c16ebd0f4f99d906f36b7de8c3c"

PYPI_PACKAGE = "hypothesis"

inherit pypi setuptools3

SRC_URI[sha256sum] = "88b0736a5691b68b8e16a4b7ee8e4c8596810c5a20989ea5b5f64870a7c25740"

RDEPENDS_${PN} += " \
    python3-attrs \
    python3-compression \
    python3-core \
    python3-json \
    python3-sortedcontainers \
    python3-statistics \
    python3-unittest \
    "

BBCLASSEXTEND = "native nativesdk"
