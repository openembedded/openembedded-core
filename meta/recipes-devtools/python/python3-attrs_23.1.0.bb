SUMMARY = "Classes Without Boilerplate"
HOMEPAGE = "http://www.attrs.org/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5e55731824cf9205cfabeab9a0600887"

SRC_URI[sha256sum] = "6279836d581513a26f1bf235f9acd333bc9115683f14f7e8fae46c98fc50e015"

inherit pypi python_hatchling

DEPENDS += " \
    ${PYTHON_PN}-hatch-vcs-native \
    ${PYTHON_PN}-hatch-fancy-pypi-readme-native \
"

RDEPENDS:${PN}+= " \
    ${PYTHON_PN}-ctypes \
    ${PYTHON_PN}-crypt \
"

BBCLASSEXTEND = "native nativesdk"
