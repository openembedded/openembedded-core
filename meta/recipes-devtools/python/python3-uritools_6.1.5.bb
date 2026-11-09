SUMMARY = "URI parsing, classification and composition"
HOMEPAGE = "https://github.com/tkem/uritools/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=079933dfba36eb60b5e3512ca0ab61ae"

SRC_URI[sha256sum] = "5a46002351b745a6910f24167abc1ba0a7f78818121fffb32dbe1fc7767f462f"

inherit pypi python_setuptools_build_meta ptest-python-pytest

DEPENDS += "python3-setuptools-scm-native"

BBCLASSEXTEND = "native nativesdk"
