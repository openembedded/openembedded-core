SUMMARY = "Convert SHACL model to code bindings"
HOMEPAGE = "https://pypi.org/project/shacl2code/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0582f358628f299f29c23bf5fb2f73c9"

PYPI_PACKAGE = "shacl2code"
SRC_URI[sha256sum] = "2dd0b7a4fd50958d0783764da6ca9ede1805ccdd22b6bed3afbf89136e8962be"

inherit pypi python_hatchling

RDEPENDS:${PN} += " \
    python3-jinja2 \
    python3-rdflib \
"

BBCLASSEXTEND = "native nativesdk"
