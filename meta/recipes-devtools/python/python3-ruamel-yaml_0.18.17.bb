SUMMARY = "YAML parser/emitter that supports roundtrip preservation of comments, seq/map flow style, and map key order."
HOMEPAGE = "https://pypi.org/project/ruamel.yaml/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5cc5d45e8a30c81dade6ca1928caa515"

PYPI_PACKAGE = "ruamel_yaml"
UPSTREAM_CHECK_PYPI_PACKAGE = "${PYPI_PACKAGE}"

inherit pypi python_setuptools_build_meta

S = "${UNPACKDIR}/ruamel.yaml-${PV}"
SRC_URI[sha256sum] = "9091cd6e2d93a3a4b157ddb8fabf348c3de7f1fb1381346d985b6b247dcd8d3c"

RDEPENDS:${PN} += "\
    python3-shell \
    python3-datetime \
    python3-netclient \
"

BBCLASSEXTEND = "native nativesdk"
