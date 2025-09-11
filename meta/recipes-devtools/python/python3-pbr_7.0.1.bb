SUMMARY = "Python Build Reasonableness"
DESCRIPTION = "PBR is a library that injects some useful and sensible default behaviors into your setuptools run"
HOMEPAGE = "https://pypi.org/project/pbr"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1dece7821bf3fd70fe1309eaa37d52a2"

inherit pypi python_setuptools_build_meta

RDEPENDS:${PN} += "python3-pip"

BBCLASSEXTEND = "native nativesdk"

SRC_URI[sha256sum] = "3ecbcb11d2b8551588ec816b3756b1eb4394186c3b689b17e04850dfc20f7e57"
