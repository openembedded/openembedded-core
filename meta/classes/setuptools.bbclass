inherit distutils

DEPENDS += "python-distribute-native"

DISTUTILS_INSTALL_ARGS = "--root=${D} \
    --prefix=${prefix} \
    --install-lib=${PYTHON_SITEPACKAGES_DIR} \
    --install-data=${datadir}"

SECURITY_CFLAGS = "${SECURITY_NO_PIE_CFLAGS}"
