require python-pycryptodome.inc
inherit setuptools3

SRC_URI[sha256sum] = "2d8bda8f949b79b78b293706aa7fc1e5c171c62661252bfdd5d12c70acd03282"

FILES:${PN}-tests = " \
    ${PYTHON_SITEPACKAGES_DIR}/Cryptodome/SelfTest/ \
    ${PYTHON_SITEPACKAGES_DIR}/Cryptodome/SelfTest/__pycache__/ \
"
