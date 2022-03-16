inherit python_pep517 python3native setuptools3-base

DEPENDS += "python3-poetry-core-native"

PEP517_BUILD_API = "poetry.core.masonry.api"

python_poetry_core_do_configure () {
    :
}

EXPORT_FUNCTIONS do_configure
