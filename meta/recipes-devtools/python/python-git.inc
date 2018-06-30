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

SRC_URI[md5sum] = "e0c773be32ba9cb57a1d703b590f6d3f"
SRC_URI[sha256sum] = "b60b045cf64a321e5b620debb49890099fa6c7be6dfb7fb249027e5d34227301"

DEPENDS = "${PYTHON_PN}-gitdb"

RDEPENDS_${PN} += " \
                   ${PYTHON_PN}-datetime \
                   ${PYTHON_PN}-gitdb \
                   ${PYTHON_PN}-io \
                   ${PYTHON_PN}-logging \
                   ${PYTHON_PN}-math \
                   ${PYTHON_PN}-netclient \
                   ${PYTHON_PN}-stringold \
                   ${PYTHON_PN}-unittest \
                   ${PYTHON_PN}-unixadmin \
                   git \
"
BBCLASSEXTEND = "nativesdk"
