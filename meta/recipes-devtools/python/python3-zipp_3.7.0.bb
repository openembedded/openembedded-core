DESCRIPTION = "Backport of pathlib-compatible object wrapper for zip files"
HOMEPAGE = "https://github.com/jaraco/zipp"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=7a7126e068206290f3fe9f8d6c713ea6"

SRC_URI[sha256sum] = "9f50f446828eb9d45b267433fd3e9da8d801f614129124863f9c51ebceafb87d"

DEPENDS += "${PYTHON_PN}-setuptools-scm-native"

inherit pypi python_setuptools_build_meta

SRC_URI += " \
    file://0001-Add-SanitizedNames-mixin.patch \
    file://0002-Employ-SanitizedNames-in-CompleteDirs.-Fixes-broken-.patch \
    file://0003-Removed-SanitizedNames.patch \
    file://0004-Address-infinite-loop-when-zipfile-begins-with-more-.patch \
    file://0005-Prefer-simpler-path.rstrip-to-consolidate-checks-for.patch \
"

DEPENDS += "${PYTHON_PN}-toml-native"

RDEPENDS:${PN} += "${PYTHON_PN}-compression \
                   ${PYTHON_PN}-math \
                   ${PYTHON_PN}-more-itertools"

BBCLASSEXTEND = "native nativesdk"
