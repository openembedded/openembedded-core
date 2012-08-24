inherit distutils

DEPENDS += "python-setuptools-native"

DISTUTILS_INSTALL_ARGS = "--root=${D} \
    --single-version-externally-managed \
    --prefix=${prefix} \
    --install-lib=${PYTHON_SITEPACKAGES_DIR} \
    --install-data=${datadir}"
