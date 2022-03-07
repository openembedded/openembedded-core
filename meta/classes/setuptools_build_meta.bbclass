inherit pip_install_wheel setuptools3-base

DEPENDS += "python3-setuptools-native python3-wheel-native"

setuptools_build_meta_do_configure () {
    :
}

# TODO: ideally this uses pypa/build
setuptools_build_meta_do_compile () {
    mkdir -p ${S}/dist
    nativepython3 -c "from setuptools import build_meta; build_meta.build_wheel('./dist')"
}

EXPORT_FUNCTIONS do_configure do_compile
