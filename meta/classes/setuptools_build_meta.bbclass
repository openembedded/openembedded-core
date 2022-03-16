inherit setuptools3-base python_pep517

DEPENDS += "python3-setuptools-native python3-wheel-native"

PEP517_BUILD_API = "setuptools.build_meta"

setuptools_build_meta_do_configure () {
    :
}

EXPORT_FUNCTIONS do_configure
