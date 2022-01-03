SUMMARY = "Pygments is a syntax highlighting package written in Python."
DESCRIPTION = "Pygments is a syntax highlighting package written in Python."
HOMEPAGE = "http://pygments.org/"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=98419e351433ac106a24e3ad435930bc"

inherit setuptools3
SRC_URI[sha256sum] = "59b895e326f0fb0d733fd28c6839bd18ad0687ba20efc26d4277fd1d30b971f4"

DEPENDS += "\
            ${PYTHON_PN} \
            "

PYPI_PACKAGE = "Pygments"

inherit pypi

BBCLASSEXTEND = "native nativesdk"

