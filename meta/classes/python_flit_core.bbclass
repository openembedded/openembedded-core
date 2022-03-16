inherit python_pep517 python3native python3-dir setuptools3-base

DEPENDS += "python3 python3-flit-core-native"

PEP517_BUILD_API = "flit_core.buildapi"

python_flit_core_do_configure () {
    :
}

EXPORT_FUNCTIONS do_configure
