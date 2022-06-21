SUMMARY = "An implementation of JSON Schema validation for Python"
HOMEPAGE = "https://github.com/Julian/jsonschema"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=7a60a81c146ec25599a3e1dabb8610a8 \
                    file://json/LICENSE;md5=9d4de43111d33570c8fe49b4cb0e01af"

SRC_URI[sha256sum] = "9d6397ba4a6c0bf0300736057f649e3e12ecbc07d3e81a0dacb72de4e9801957"

inherit pypi python_hatchling

DEPENDS += "${PYTHON_PN}-hatch-vcs-native"

PACKAGECONFIG ??= "format"
PACKAGECONFIG[format] = ",,,\
    ${PYTHON_PN}-idna \
    ${PYTHON_PN}-jsonpointer \
    ${PYTHON_PN}-webcolors \
    ${PYTHON_PN}-rfc3987 \
    ${PYTHON_PN}-strict-rfc3339 \
"
PACKAGECONFIG[nongpl] = ",,,\
    ${PYTHON_PN}-idna \
    ${PYTHON_PN}-jsonpointer \
    ${PYTHON_PN}-webcolors \
    ${PYTHON_PN}-rfc3986-validator \
    ${PYTHON_PN}-rfc3339-validator \
"

RDEPENDS:${PN} += " \
    ${PYTHON_PN}-attrs \
    ${PYTHON_PN}-core \
    ${PYTHON_PN}-datetime \
    ${PYTHON_PN}-importlib-metadata \
    ${PYTHON_PN}-io \
    ${PYTHON_PN}-json \
    ${PYTHON_PN}-netclient \
    ${PYTHON_PN}-numbers \
    ${PYTHON_PN}-pkgutil \
    ${PYTHON_PN}-pprint \
    ${PYTHON_PN}-pyrsistent \
    ${PYTHON_PN}-shell \
    ${PYTHON_PN}-six \
    ${PYTHON_PN}-unittest \
    ${PYTHON_PN}-setuptools-scm \
    ${PYTHON_PN}-zipp \
"

BBCLASSEXTEND = "native nativesdk"
