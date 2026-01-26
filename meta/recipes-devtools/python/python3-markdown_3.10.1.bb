SUMMARY = "A Python implementation of John Gruber's Markdown."
HOMEPAGE = "https://python-markdown.github.io/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=ec58cdf7cfed06a21f7a9362627a5480"

inherit pypi python_setuptools_build_meta

SRC_URI[sha256sum] = "1c19c10bd5c14ac948c53d0d762a04e2fa35a6d58a6b7b1e6bfcbe6fefc0001a"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "python3-logging python3-setuptools"
