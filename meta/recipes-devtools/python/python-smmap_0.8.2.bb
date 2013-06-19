SUMMARY = "Python implementation of a sliding window memory map manager"
DESCRIPTION = "A pure Python implementation of a sliding memory map to \
help unifying memory mapped access on 32 and 64 bit systems and to help \
managing resources more efficiently."
HOMEPAGE = "http://github.com/gitpython-developers/GitPython"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://PKG-INFO;beginline=8;endline=8;md5=e910b35b0ef4e1f665b9a75d6afb7709"

SRC_URI = "http://pypi.python.org/packages/source/s/smmap/smmap-${PV}.tar.gz"
SRC_URI[md5sum] = "f5426b7626ddcf5e447253fae0396b0c"
SRC_URI[sha256sum] = "dea2955cc045ec5527da6b762f7e95a5be7f645c683b54ccce52d56b4d7e2d6f"

S = "${WORKDIR}/smmap-${PV}"

inherit setuptools

RDEPENDS_${PN} += "python-codecs python-mmap python-lang"

BBCLASSEXTEND = "nativesdk"
