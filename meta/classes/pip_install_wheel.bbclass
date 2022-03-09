DEPENDS:append = " python3-pip-native"

# The directory where wheels should be written too. Build classes
# will ideally [cleandirs] this but we don't do that here in case
# a recipe wants to install prebuilt wheels.
PIP_INSTALL_DIST_PATH ?= "${WORKDIR}/dist"

PIP_INSTALL_ARGS = "\
    -vvvv \
    --ignore-installed \
    --no-cache \
    --no-deps \
    --no-index \
    --root=${D} \
    --prefix=${prefix} \
"

PIP_INSTALL_PYTHON = "python3"
PIP_INSTALL_PYTHON:class-native = "nativepython3"

pip_install_wheel_do_install () {
    COUNT=$(find ${PIP_INSTALL_DIST_PATH} -name '*.whl' | wc -l)
    if test $COUNT -eq 0; then
        bbfatal No wheels found in ${PIP_INSTALL_DIST_PATH}
    elif test $COUNT -gt 1; then
        bbfatal More than one wheel found in ${PIP_INSTALL_DIST_PATH}, this should not happen
    fi

    nativepython3 -m pip install ${PIP_INSTALL_ARGS} ${PIP_INSTALL_DIST_PATH}/*.whl

    cd ${D}
    for i in ${D}${bindir}/* ${D}${sbindir}/*; do
        if [ -f "$i" ]; then
            sed -i -e "1s,#!.*nativepython3,#!${USRBINPATH}/env ${PIP_INSTALL_PYTHON}," $i
            sed -i -e "s:${PYTHON}:${USRBINPATH}/env\ ${PIP_INSTALL_PYTHON}:g" $i
            sed -i -e "s:${STAGING_BINDIR_NATIVE}:${bindir}:g" $i
            # Not everything we find may be Python, so ignore errors
            nativepython3 -mpy_compile $(realpath --relative-to=${D} $i) || true
        fi
    done
}

EXPORT_FUNCTIONS do_install
