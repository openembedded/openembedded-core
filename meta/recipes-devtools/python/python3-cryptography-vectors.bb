SUMMARY = "Test vectors for the cryptography package."
HOMEPAGE = "https://cryptography.io/"
SECTION = "devel/python"
LICENSE = "Apache-2.0 | BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8c3617db4fb6fae01f1d253ab91511e4 \
                    file://LICENSE.APACHE;md5=4e168cce331e5c827d4c2b68a6200e1b \
                    file://LICENSE.BSD;md5=5ae30ba4123bc4f2fa49aa0b0dce887b"

# NOTE: Make sure to keep this recipe at the same version as python3-cryptography
#       Upgrade both recipes at the same time
require python3-cryptography-common.inc
SRC_URI += "file://0001-pyproject.toml-bump-uv_build-version-requirement.patch"
SRC_URI[sha256sum] = "fb8e489f5688d9751fd9631d4be0a0df3275698e78d6d753c1b8cf3ce4127c78"

PYPI_PACKAGE = "cryptography_vectors"
UPSTREAM_CHECK_PYPI_PACKAGE = "${PYPI_PACKAGE}"

DEPENDS += "python3-uv-build-native"

inherit pypi python_flit_core

BBCLASSEXTEND = "native nativesdk"
