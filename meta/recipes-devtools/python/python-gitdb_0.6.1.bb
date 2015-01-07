SUMMARY = "A pure-Python git object database"
HOMEPAGE = "http://github.com/gitpython-developers/gitdb"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=59e5ecb13339a936eedf83282eaf4528"
DEPENDS = "python-async python-smmap"

SRC_URI = "https://pypi.python.org/packages/source/g/gitdb/gitdb-${PV}.tar.gz"

SRC_URI[md5sum] = "daeb85a5e97d2d100fd37cf522fd65d5"
SRC_URI[sha256sum] = "0d784ec4a75e93c8f65d4aca219c17e388a052d461714ed0edfda657e542b716"

S = "${WORKDIR}/gitdb-${PV}"

inherit distutils

RDEPENDS_${PN} += "python-smmap python-async python-mmap python-lang python-zlib python-io python-shell"

BBCLASSEXTEND = "nativesdk"
