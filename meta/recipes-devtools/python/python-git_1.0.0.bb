SUMMARY = "Python library used to interact with Git repositories"
DESCRIPTION = "GitPython provides object model read and write access to \
a git repository. Access repository information conveniently, alter the \
index directly, handle remotes, or go down to low-level object database \
access with big-files support."
HOMEPAGE = "http://github.com/gitpython-developers/GitPython"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8b8d26c37c1d5a04f9b0186edbebc183"
DEPENDS = "python-gitdb"

SRC_URI = "http://pypi.python.org/packages/source/G/GitPython/GitPython-${PV}.tar.gz"

SRC_URI[md5sum] = "c3833aad16d5660436e284d597ec1bec"
SRC_URI[sha256sum] = "c1b3e6fdd209040b6b1cb32ecbff989eeb2fb31b4e7078c3342d71ae8ef7352b"

S = "${WORKDIR}/GitPython-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-gitdb python-lang python-io python-shell python-math python-re python-subprocess python-stringold"

BBCLASSEXTEND = "nativesdk"
