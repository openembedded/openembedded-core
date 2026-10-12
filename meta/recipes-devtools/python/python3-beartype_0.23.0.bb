SUMMARY = "Unbearably fast runtime type checking in pure Python."
HOMEPAGE = "https://beartype.readthedocs.io"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2597b30908b2171ba6cb2080fd5be4c5"

SRC_URI[sha256sum] = "9d915ce043a2262ee3eb110d1c1c528179f06eb4145e375a750f72ddbdf40fa3"

inherit pypi python_hatchling

RDEPENDS:${PN} += "python3-numbers python3-io"

BBCLASSEXTEND = "native nativesdk"
