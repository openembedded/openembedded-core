SUMMARY = "A linter for YAML files."
HOMEPAGE = "https://github.com/adrienverge/yamllint"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

inherit pypi python_setuptools_build_meta

PYPI_PACKAGE = "yamllint"

SRC_URI[sha256sum] = "ead81921d4d87216b2528b7a055664708f9fb8267beb0c427cb706ac6ab93580"

RDEPENDS:${PN} += "python3-pathspec python3-pyyaml"

BBCLASSEXTEND = "native nativesdk"
