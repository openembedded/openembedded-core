SUMMARY = "sphinxcontrib-htmlhelp is a sphinx extension which renders HTML help files"
HOMEPAGE = "https://www.sphinx-doc.org"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=24dce5ef6a13563241c24bc366f48886"

SRC_URI[sha256sum] = "c6597da06185f0e3b4dc952777a04200611ef563882e0c244d27a15ee22afa73"

PYPI_PACKAGE = "sphinxcontrib_htmlhelp"

inherit pypi python_flit_core

BBCLASSEXTEND = "native nativesdk"
