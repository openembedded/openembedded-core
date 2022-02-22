LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=41eb78fa8a872983a882c694a8305f08"

SRC_URI[sha256sum] = "b1464e006df4df4c8eeb37671c0e0ce66e1d04e4a36d91b702f180a25fde3c11"

inherit python3native python3-dir pypi setuptools3-base

DEPENDS:append:class-target = " python3-pip-native"
DEPENDS:append:class-native = " unzip-native"

# We need the full flit tarball
PYPI_PACKAGE = "flit"

do_compile () {
    nativepython3 flit_core/build_dists.py
}

do_install () {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    PYTHONPATH=${D}${PYTHON_SITEPACKAGES_DIR} \
    nativepython3 -m pip install -vvvv --no-deps --no-index --target ${D}${PYTHON_SITEPACKAGES_DIR} ./flit_core/dist/flit_core-${PV}-py3-none-any.whl
}

do_install:class-native () {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -d ${D}${PYTHON_SITEPACKAGES_DIR} ./flit_core/dist/flit_core-${PV}-py3-none-any.whl
}

FILES:${PN} += "\
    ${PYTHON_SITEPACKAGES_DIR}/flit_core/* \
    ${PYTHON_SITEPACKAGES_DIR}/flit_core-${PV}.dist-info/* \
"

PACKAGES =+ "${PN}-tests"

FILES:${PN}-tests += "\
    ${PYTHON_SITEPACKAGES_DIR}/flit_core/tests/* \
"

BBCLASSEXTEND = "native nativesdk"

