# Common infrastructure for Python packages that use PEP-517 compliant packaging.
# https://www.python.org/dev/peps/pep-0517/

DEPENDS:append = " python3-pip-native"

# Where to execute the build process from
PEP517_SOURCE_PATH ?= "${S}"

# The directory where wheels should be written too. Build classes
# will ideally [cleandirs] this but we don't do that here in case
# a recipe wants to install prebuilt wheels.
PEP517_WHEEL_PATH ?= "${WORKDIR}/dist"

PIP_INSTALL_ARGS = "\
    -vvvv \
    --ignore-installed \
    --no-cache \
    --no-deps \
    --no-index \
    --root=${D} \
    --prefix=${prefix} \
"

PEP517_INSTALL_PYTHON = "python3"
PEP517_INSTALL_PYTHON:class-native = "nativepython3"

python_pep517_do_install () {
    COUNT=$(find ${PEP517_WHEEL_PATH} -name '*.whl' | wc -l)
    if test $COUNT -eq 0; then
        bbfatal No wheels found in ${PEP517_WHEEL_PATH}
    elif test $COUNT -gt 1; then
        bbfatal More than one wheel found in ${PEP517_WHEEL_PATH}, this should not happen
    fi

    nativepython3 -m pip install ${PIP_INSTALL_ARGS} ${PEP517_WHEEL_PATH}/*.whl

    cd ${D}
    for i in ${D}${bindir}/* ${D}${sbindir}/*; do
        if [ -f "$i" ]; then
            sed -i -e "1s,#!.*nativepython3,#!${USRBINPATH}/env ${PEP517_INSTALL_PYTHON}," $i
            sed -i -e "s:${PYTHON}:${USRBINPATH}/env\ ${PEP517_INSTALL_PYTHON}:g" $i
            sed -i -e "s:${STAGING_BINDIR_NATIVE}:${bindir}:g" $i
            # Not everything we find may be Python, so ignore errors
            nativepython3 -mpy_compile $(realpath --relative-to=${D} $i) || true
        fi
    done
}

# A manual do_install that just uses unzip for bootstrapping purposes. Callers should DEPEND on unzip-native.
python_pep517_do_bootstrap_install () {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
    unzip -d ${D}${PYTHON_SITEPACKAGES_DIR} ${PEP517_WHEEL_PATH}/*.whl
}

EXPORT_FUNCTIONS do_install
