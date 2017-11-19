SUMMARY = "Python library used to interact with Git repositories"
DESCRIPTION = "GitPython provides object model read and write access to \
a git repository. Access repository information conveniently, alter the \
index directly, handle remotes, or go down to low-level object database \
access with big-files support."
HOMEPAGE = "http://github.com/gitpython-developers/GitPython"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8b8d26c37c1d5a04f9b0186edbebc183"

PYPI_PACKAGE = "GitPython"

inherit pypi

SRC_URI[md5sum] = "a63fdd2a650694ea23e268fd0c952034"
SRC_URI[sha256sum] = "13c7cd99c2bf277fc99accfc25148752fa90e7cc6c6d08a3f01d229dacb461d9"

DEPENDS = "${PYTHON_PN}-gitdb"

RDEPENDS_${PN} += "${PYTHON_PN}-argparse \
                   ${PYTHON_PN}-datetime \
                   ${PYTHON_PN}-enum \
                   ${PYTHON_PN}-gitdb \
                   ${PYTHON_PN}-io \
                   ${PYTHON_PN}-lang \
                   ${PYTHON_PN}-logging \
                   ${PYTHON_PN}-math \
                   ${PYTHON_PN}-netclient \
                   ${PYTHON_PN}-re \
                   ${PYTHON_PN}-shell \
                   ${PYTHON_PN}-stringold \
                   ${PYTHON_PN}-subprocess \
                   ${PYTHON_PN}-unittest \
                   ${PYTHON_PN}-unixadmin \
                   git \
"
BBCLASSEXTEND = "nativesdk"
