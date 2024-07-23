require python-pycryptodome.inc
inherit python_setuptools_build_meta

SRC_URI[sha256sum] = "e04e40a7f8c1669195536a37979dd87da2c32dbdc73d6fe35f0077b0c17c803b"

SRC_URI += "file://CVE-2023-52323.patch"
