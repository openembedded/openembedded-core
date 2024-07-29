SUMMARY = "Is a sphinx extension which outputs QtHelp document."
HOMEPAGE = "http://babel.edgewall.org/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f7a83b72ea86d04827575ec0b63430eb"

SRC_URI[sha256sum] = "db3f8fa10789c7a8e76d173c23364bdf0ebcd9449969a9e6a3dd31b8b7469f03"

PYPI_PACKAGE = "sphinxcontrib_qthelp"

inherit pypi python_flit_core

BBCLASSEXTEND = "native nativesdk"
