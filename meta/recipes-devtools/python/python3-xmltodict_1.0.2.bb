SUMMARY = "Makes working with XML feel like you are working with JSON"
HOMEPAGE = "https://github.com/martinblech/xmltodict"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=01441d50dc74476db58a41ac10cb9fa2"

SRC_URI[sha256sum] = "54306780b7c2175a3967cad1db92f218207e5bc1aba697d887807c0fb68b7649"

PYPI_PACKAGE = "xmltodict"

BBCLASSEXTEND = "native nativesdk"

inherit pypi python_setuptools_build_meta ptest-python-pytest

RDEPENDS:${PN} += " \
	python3-core \
	python3-xml \
	python3-io \
"

