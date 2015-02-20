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

SRC_URI[md5sum] = "87404ab9e0fcee81e660e932b3d9f2c6"
SRC_URI[sha256sum] = "f3f42ca085eedbd3a9956b5e639de58bbe77a119f6b3d5c3af27669a2322c4a9"

S = "${WORKDIR}/GitPython-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-gitdb python-lang python-io python-shell python-math python-re python-subprocess python-stringold"

BBCLASSEXTEND = "nativesdk"
