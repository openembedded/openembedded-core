SUMMARY = "A pure-Python git object database"
HOMEPAGE = "http://github.com/gitpython-developers/gitdb"
SECTION = "devel/python"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=59e5ecb13339a936eedf83282eaf4528"
DEPENDS = "python-async python-smmap"

SRC_URI = "https://pypi.python.org/packages/source/g/gitdb/gitdb-${PV}.tar.gz"
SRC_URI[md5sum] = "25353bb8d3ea527ba443dd88cd4e8a1c"
SRC_URI[sha256sum] = "de5d2dac0daec4a9cd7bb1ae1cd42d53510dcf597397c608c12a154b69ad3783"

S = "${WORKDIR}/gitdb-${PV}"

inherit distutils

RDEPENDS_${PN} += "python-smmap python-async python-mmap python-lang python-zlib python-io python-shell"

BBCLASSEXTEND = "nativesdk"
