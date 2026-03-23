DESCRIPTION = "Pyzstd module provides classes and functions for compressing and \
decompressing data, using Facebook’s Zstandard (or zstd as short name) algorithm."
SUMMARY = "Python bindings to Zstandard (zstd) compression library"
HOMEPAGE = "https://github.com/animalize/pyzstd"
SECTION = "devel/python"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aedb5a2679cd1552fb61c181ef974b9e"

PYPI_PACKAGE = "pyzstd"

SRC_URI[sha256sum] = "36723d3c915b3981de9198d0a2c82b2f5fe3eaa36e4d8d586937830a8afc7d72"

inherit pypi python_hatchling ptest-python-pytest

DEPENDS += "python3-hatch-vcs-native"

