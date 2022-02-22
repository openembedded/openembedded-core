DEPENDS:append = " python3-pip-native"

PIP_INSTALL_PACKAGE ?= "${PYPI_PACKAGE}"
PIP_INSTALL_DIST_PATH ?= "${B}/dist"
PYPA_WHEEL ??= "${PIP_INSTALL_DIST_PATH}/${PIP_INSTALL_PACKAGE}-${PV}-*.whl"

PIP_INSTALL_ARGS ?= "\
    -vvvv \
    --force-reinstall \
    --no-cache \
    --no-deps \
    --no-index \
    --root=${D} \
    --prefix=${prefix} \
"

pip_install_wheel_do_install:prepend () {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
}

export PYPA_WHEEL

PIP_INSTALL_PYTHON = "python3"
PIP_INSTALL_PYTHON:class-native = "nativepython3"

pip_install_wheel_do_install () {
    nativepython3 -m pip install ${PIP_INSTALL_ARGS} ${PYPA_WHEEL} ||
    bbfatal_log "Failed to pip install wheel. Check the logs."

    for i in ${D}${bindir}/* ${D}${sbindir}/*; do
        if [ -f "$i" ]; then
            sed -i -e "1s,#!.*nativepython3,#!${USRBINPATH}/env ${PIP_INSTALL_PYTHON}," $i
            sed -i -e "s:${PYTHON}:${USRBINPATH}/env\ ${PIP_INSTALL_PYTHON}:g" $i
            sed -i -e "s:${STAGING_BINDIR_NATIVE}:${bindir}:g" $i
        fi
    done
}

EXPORT_FUNCTIONS do_install
