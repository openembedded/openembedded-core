SUMMARY = "A modern Python package and dependency manager supporting the latest PEP standards"
HOMEPAGE = "https://pdm-project.org/latest/"
LICENSE = "MIT"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eb31a2cc1a758c34b499f287dd04ef2"

SRC_URI[sha256sum] = "dc31c1d11c2e90564207181f87ca3d6fc16ee6584d703c0a9e0c03ba287a925a"

inherit pypi python_pdm

DEPENDS += " \
    python3-pdm-build-locked-native \
"

BBCLASSEXTEND = "native nativesdk"
