DEPENDS:append = " python3-pip-native"

def guess_pip_install_package_name(d):
    import re
    '''https://www.python.org/dev/peps/pep-0491/#escaping-and-unicode'''
    name = d.getVar('PYPI_PACKAGE') or re.sub(r"^python3-", "", d.getVar('BPN'))
    return name.replace('-', '_')

PIP_INSTALL_PACKAGE ?= "${@guess_pip_install_package_name(d)}"
PIP_INSTALL_DIST_PATH ?= "${@d.getVar('SETUPTOOLS_SETUP_PATH') or d.getVar('B')}/dist"
PYPA_WHEEL ??= "${PIP_INSTALL_DIST_PATH}/${PIP_INSTALL_PACKAGE}-*-*.whl"

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
    nativepython3 -m pip install ${PIP_INSTALL_ARGS} ${PYPA_WHEEL} ||
      bbfatal_log "Failed to pip install wheel. Check the logs."

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
