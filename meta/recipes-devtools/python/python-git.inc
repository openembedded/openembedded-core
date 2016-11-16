SUMMARY = "Python library used to interact with Git repositories"
DESCRIPTION = "GitPython provides object model read and write access to \
a git repository. Access repository information conveniently, alter the \
index directly, handle remotes, or go down to low-level object database \
access with big-files support."
HOMEPAGE = "http://github.com/gitpython-developers/GitPython"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=8b8d26c37c1d5a04f9b0186edbebc183"

SRC_URI = "https://files.pythonhosted.org/packages/source/G/GitPython/GitPython-${PV}.tar.gz"

SRC_URI[md5sum] = "29b1fcf504d080dc7a5e630957e829d7"
SRC_URI[sha256sum] = "3ebda1e6ff1ef68597e41dcd1b99c2a5ae902f4dc2b22ad3533cc89c32b42aad"

UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/GitPython/"
UPSTREAM_CHECK_REGEX = "/GitPython/(?P<pver>(\d+[\.\-_]*)+)"

S = "${WORKDIR}/GitPython-${PV}"

BBCLASSEXTEND = "nativesdk"
