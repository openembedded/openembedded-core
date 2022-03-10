inherit pip_install_wheel python3native python3-dir setuptools3-base

DEPENDS += "python3 python3-flit-core-native"

flit_core_do_configure () {
    :
}

# Where to execute the build process from
PEP517_SOURCE_PATH ?= "${S}"

# TODO: ideally this uses pypa/build
flit_core_do_compile () {
    cd ${PEP517_SOURCE_PATH}
    nativepython3 -mflit_core.wheel --outdir ${PIP_INSTALL_DIST_PATH}
}
do_compile[cleandirs] += "${PIP_INSTALL_DIST_PATH}"

EXPORT_FUNCTIONS do_configure do_compile
