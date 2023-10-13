SUMMARY = "Unbearably fast runtime type checking in pure Python."
HOMEPAGE = "https://beartype.readthedocs.io"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=e40b52d8eb5553aa8f705cdd3f979d69"

SRC_URI[sha256sum] = "47ec1c8c3be3f999f4f9f829e8913f65926aa7e85b180d9ffd305dc78d3e7d7b"

inherit setuptools3 pypi

BBCLASSEXTEND = "native nativesdk"
