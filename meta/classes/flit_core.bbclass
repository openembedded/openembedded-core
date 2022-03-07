inherit pip_install_wheel python3native python3-dir setuptools3-base

DEPENDS += "python3 python3-flit-core-native python3-pip-native"

flit_core_do_configure () {
    :
}

# TODO: ideally this uses pypa/build
flit_core_do_compile () {
    mkdir -p ${S}/dist
    nativepython3 -c "from flit_core import buildapi; buildapi.build_wheel('./dist')"
}

EXPORT_FUNCTIONS do_configure do_compile
