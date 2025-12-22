SUMMARY = "PEP 621 metadata parsing"
DESCRIPTION = "Dataclass for PEP 621 metadata with support for core \
metadata generation \
\
This project does not implement the parsing of pyproject.toml containing \
PEP 621 metadata.\
\
Instead, given a Python data structure representing PEP 621 metadata \
(already parsed), it will validate this input and generate a \
PEP 643-compliant metadata file (e.g. PKG-INFO)."
HOMEPAGE = "https://github.com/FFY00/python-pyproject-metadata"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=310439af287b0fb4780b2ad6907c256c"

PYPI_PACKAGE = "pyproject_metadata"
UPSTREAM_CHECK_PYPI_PACKAGE = "${PYPI_PACKAGE}"

inherit pypi python_flit_core

SRC_URI[sha256sum] = "7f5bd0ef398b60169556cb17ea261d715caf7f8561238151f51b2305084ba8d4"

RDEPENDS:${PN} += " \
    python3-logging \
    python3-packaging \
    python3-profile \
"

BBCLASSEXTEND = "native nativesdk"
