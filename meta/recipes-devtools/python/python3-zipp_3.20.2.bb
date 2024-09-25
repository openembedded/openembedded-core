SUMMARY = "Backport of pathlib-compatible object wrapper for zip files"
HOMEPAGE = "https://github.com/jaraco/zipp"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=141643e11c48898150daa83802dbc65f"

SRC_URI[sha256sum] = "bc9eb26f4506fda01b81bcde0ca78103b6e62f991b381fec825435c836edbc29"

DEPENDS += "python3-setuptools-scm-native"

inherit pypi python_setuptools_build_meta

RDEPENDS:${PN} += "python3-compression \
                   python3-math"

BBCLASSEXTEND = "native nativesdk"
