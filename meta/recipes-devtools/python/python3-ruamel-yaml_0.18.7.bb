SUMMARY = "YAML parser/emitter that supports roundtrip preservation of comments, seq/map flow style, and map key order."
HOMEPAGE = "https://pypi.org/project/ruamel.yaml/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=30cbbccd94bf3a2b0285ec35671a1938"

PYPI_PACKAGE = "ruamel.yaml"
UPSTREAM_CHECK_PYPI_PACKAGE = "${PYPI_PACKAGE}"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "270638acec6659f7bb30f4ea40083c9a0d0d5afdcef5e63d666f11209091531a"

RDEPENDS:${PN} += "\
    python3-shell \
    python3-datetime \
    python3-netclient \
"

BBCLASSEXTEND = "native nativesdk"
