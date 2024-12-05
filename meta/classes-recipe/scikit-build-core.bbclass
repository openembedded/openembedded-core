DEPENDS += "scikit-build-core-native ninja-native"

inherit python_pep517 python3native python3targetconfig

export PYTHONPATH="${STAGING_LIBDIR}/python-sysconfigdata"
