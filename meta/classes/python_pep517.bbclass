# Common infrastructure for Python packages that use PEP-517 compliant packaging.
# https://www.python.org/dev/peps/pep-0517/

DEPENDS:append = " python3-installer-native"

# Where to execute the build process from
PEP517_SOURCE_PATH ?= "${S}"

# The directory where wheels should be written too. Build classes
# will ideally [cleandirs] this but we don't do that here in case
# a recipe wants to install prebuilt wheels.
PEP517_WHEEL_PATH ?= "${WORKDIR}/dist"

PEP517_INSTALL_PYTHON = "python3"
PEP517_INSTALL_PYTHON:class-native = "nativepython3"

INSTALL_WHEEL_COMPILE_BYTECODE ?= "--compile-bytecode=0"

python_pep517_do_install () {
    COUNT=$(find ${PEP517_WHEEL_PATH} -name '*.whl' | wc -l)
    if test $COUNT -eq 0; then
        bbfatal No wheels found in ${PEP517_WHEEL_PATH}
    elif test $COUNT -gt 1; then
        bbfatal More than one wheel found in ${PEP517_WHEEL_PATH}, this should not happen
    fi

    nativepython3 -m installer ${INSTALL_WHEEL_COMPILE_BYTECODE} --interpreter "${USRBINPATH}/env ${PEP517_INSTALL_PYTHON}" --destdir=${D} ${PEP517_WHEEL_PATH}/*.whl
}

# A manual do_install that just uses unzip for bootstrapping purposes. Callers should DEPEND on unzip-native.
python_pep517_do_bootstrap_install () {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -d ${D}${PYTHON_SITEPACKAGES_DIR} ${PEP517_WHEEL_PATH}/*.whl
}

EXPORT_FUNCTIONS do_install
