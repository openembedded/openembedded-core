SUMMARY = "the blessed package to manage your versions by scm tags"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://PKG-INFO;beginline=8;endline=8;md5=8227180126797a0148f94f483f3e1489"

SRC_URI[md5sum] = "e6c9fad17c90516d640868eb833d5150"
SRC_URI[sha256sum] = "a8994582e716ec690f33fec70cca0f85bd23ec974e3f783233e4879090a7faa8"

PYPI_PACKAGE = "setuptools_scm"
inherit pypi setuptools3

UPSTREAM_CHECK_REGEX = "setuptools_scm-(?P<pver>.*)\.tar"

RDEPENDS_${PN} = "\
    ${PYTHON_PN}-debugger \
    ${PYTHON_PN}-json \
    ${PYTHON_PN}-py \
    ${PYTHON_PN}-setuptools \
    ${PYTHON_PN}-toml \
"
RDEPENDS_${PN}_class-native = "\
    ${PYTHON_PN}-setuptools-native \
    ${PYTHON_PN}-toml-native \
"

BBCLASSEXTEND = "native nativesdk"
