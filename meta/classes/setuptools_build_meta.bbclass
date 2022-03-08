inherit pip_install_wheel setuptools3-base

DEPENDS += "python3-setuptools-native python3-wheel-native"

setuptools_build_meta_do_configure () {
    :
}

# TODO: ideally this uses pypa/build
setuptools_build_meta_do_compile () {
    nativepython3 -c "from setuptools import build_meta; build_meta.build_wheel('${PIP_INSTALL_DIST_PATH}')"
}
do_compile[cleandirs] += "${PIP_INSTALL_DIST_PATH}"

EXPORT_FUNCTIONS do_configure do_compile
