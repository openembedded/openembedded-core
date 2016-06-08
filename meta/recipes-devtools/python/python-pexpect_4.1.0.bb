SUMMARY = "A Pure Python Expect like Module for Python"
HOMEPAGE = "http://pexpect.readthedocs.org/"
SECTION = "devel/python"
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://LICENSE;md5=66c2378a96b307d56bfb3a9e58edafa8"

SRCNAME = "pexpect"

SRC_URI = "https://files.pythonhosted.org/packages/source/p/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"
SRC_URI[md5sum] = "562a1a21f2a60b36dfd5d906dbf0943e"
SRC_URI[sha256sum] = "c381c60f1987355b65df8f08a27f428831914c8a81091bd1778ac336fa2f27e7"

UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/pexpect"

S = "${WORKDIR}/pexpect-${PV}"

inherit setuptools

RDEPENDS_${PN} = "\
    python-core \
    python-io \
    python-terminal \
    python-resource \
    python-fcntl \
    python-ptyprocess \
"

BBCLASSEXTEND = "nativesdk"
