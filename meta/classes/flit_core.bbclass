inherit python_pep517 python3native python3-dir setuptools3-base

DEPENDS += "python3 python3-flit-core-native"

flit_core_do_configure () {
    :
}

# TODO: ideally this uses pypa/build
flit_core_do_compile () {
    cd ${PEP517_SOURCE_PATH}
    nativepython3 -mflit_core.wheel --outdir ${PEP517_WHEEL_PATH}
}
do_compile[cleandirs] += "${PEP517_WHEEL_PATH}"

EXPORT_FUNCTIONS do_configure do_compile
