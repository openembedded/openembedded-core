SUMMARY = "A modern Python package and dependency manager supporting the latest PEP standards"
HOMEPAGE = "https://pdm-project.org/latest/"
LICENSE = "MIT"
SECTION = "devel/python"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2eb31a2cc1a758c34b499f287dd04ef2"

SRC_URI[sha256sum] = "6d0820f805dacf64d55a7fe56777e7d8349a2ee35efc3006f29b4573d1311c84"

inherit pypi python_setuptools_build_meta

DEPENDS += " \
    python3-pdm-backend-native \
    python3-pdm-build-locked-native \
"

BBCLASSEXTEND = "native nativesdk"
