DESCRIPTION = "Parses image files’ header and return image size."
HOMEPAGE = "https://github.com/shibukawa/imagesize_py"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.rst;md5=0c128f0f7e8a02e1b83884c0b5a41cda"

SRC_URI[sha256sum] = "b1f6b5a4eab1f73479a50fb79fcf729514a900c341d8503d62a62dbc4127a2b1"

inherit setuptools3 pypi

BBCLASSEXTEND = "native nativesdk"
