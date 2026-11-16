SUMMARY = "A sphinx extension for designing beautiful, view size responsive web components"
HOMEPAGE = "https://sphinx-design.readthedocs.io/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=484a29024499c025d11c52f22a6f0bb8"

SRC_URI[sha256sum] = "57fa4d96378525122947ab6109c6e490190e3119dc10f3e3adf6b71444bf3bb4"

inherit pypi python_flit_core

PYPI_PACKAGE = "sphinx_design"

BBCLASSEXTEND = "native nativesdk"
