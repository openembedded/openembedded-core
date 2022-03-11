inherit setuptools3-base python_pep517

DEPENDS += "python3-setuptools-native python3-wheel-native"

setuptools_build_meta_do_configure () {
    :
}

# TODO: ideally this uses pypa/build
setuptools_build_meta_do_compile () {
    cd ${PEP517_SOURCE_PATH}
    nativepython3 -c "from setuptools import build_meta; build_meta.build_wheel('${PEP517_WHEEL_PATH}')"
}
do_compile[cleandirs] += "${PEP517_WHEEL_PATH}"

EXPORT_FUNCTIONS do_configure do_compile
