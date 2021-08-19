SUMMARY = "A library for property-based testing"
HOMEPAGE = "https://github.com/HypothesisWorks/hypothesis/tree/master/hypothesis-python"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=4ee62c16ebd0f4f99d906f36b7de8c3c"

PYPI_PACKAGE = "hypothesis"

inherit pypi setuptools3

SRC_URI[sha256sum] = "cb80e06ca44992b08d2889734b740e67c8b41337315bbd6dba2ef43ca816cfe3"

RDEPENDS:${PN} += " \
    python3-attrs \
    python3-compression \
    python3-core \
    python3-json \
    python3-sortedcontainers \
    python3-statistics \
    python3-unittest \
    "

BBCLASSEXTEND = "native nativesdk"
