SUMMARY = "The build backend used by PDM that supports latest packaging standards"
HOMEPAGE = "https://github.com/pdm-project/pdm-backend"
LICENSE = "MIT"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4a564297b3c5b629a528b92fd8ff61ea"

SRC_URI[sha256sum] = "551b049379d4f270cba18f5bf73031cc229a4f883e085265465d326d4a636861"

inherit pypi python_pep517

PYPI_PACKAGE = "pdm_backend"

BBCLASSEXTEND = "native nativesdk"
