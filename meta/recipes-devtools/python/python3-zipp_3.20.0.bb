SUMMARY = "Backport of pathlib-compatible object wrapper for zip files"
HOMEPAGE = "https://github.com/jaraco/zipp"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=141643e11c48898150daa83802dbc65f"

SRC_URI[sha256sum] = "0145e43d89664cfe1a2e533adc75adafed82fe2da404b4bbb6b026c0157bdb31"

DEPENDS += "python3-setuptools-scm-native"

inherit pypi python_setuptools_build_meta

RDEPENDS:${PN} += "python3-compression \
                   python3-math"

BBCLASSEXTEND = "native nativesdk"
