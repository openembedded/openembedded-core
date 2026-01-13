SUMMARY = "A modern Python package and dependency manager supporting the latest PEP standards"
HOMEPAGE = "https://pdm-project.org/latest/"
LICENSE = "MIT"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eb31a2cc1a758c34b499f287dd04ef2"

SRC_URI[sha256sum] = "d1030f6bd8e9449b5791e7768f1a856cc54617ee7b00f56fa1a843a84ad77f6e"

inherit pypi python_pdm

DEPENDS += " \
    python3-pdm-build-locked-native \
"

BBCLASSEXTEND = "native nativesdk"
