inherit pip_install_wheel python3native python3-dir setuptools3-base

DEPENDS += "python3 python3-flit-core-native python3-pip-native"

flit_core_do_configure () {
    mkdir -p ${S}/dist
    cat > ${S}/build-it.py << EOF
from flit_core import buildapi
buildapi.build_wheel('./dist')
EOF
}

flit_core_do_compile () {
    ${STAGING_BINDIR_NATIVE}/${PYTHON_PN}-native/${PYTHON_PN} ${S}/build-it.py
}

EXPORT_FUNCTIONS do_configure do_compile
