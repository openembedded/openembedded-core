SUMMARY = "Convert SHACL model to code bindings"
HOMEPAGE = "https://pypi.org/project/shacl2code/"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0582f358628f299f29c23bf5fb2f73c9"

PYPI_PACKAGE = "shacl2code"
SRC_URI[sha256sum] = "92c5bb633b232bb914699157b3e79796aaf1672f22c0cac0cc633f353aa8a398"

inherit pypi python_hatchling

RDEPENDS:${PN} += " \
    python3-jinja2 \
    python3-rdflib \
"

BBCLASSEXTEND = "native nativesdk"
