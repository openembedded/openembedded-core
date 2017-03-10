HOMEPAGE = "https://github.com/peterjc/backports.lzma"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=846e05e7e9e1c886b2637c230cfcd5dd"

SRC_URI = "git://github.com/peterjc/backports.lzma.git \
           file://0001-setup.py-do-not-add-include-and-library-directories-.patch \
           "

PV = "0.0.6+git${SRCPV}"
SRCREV = "718b3316ae7aee8e03c02e7e110108779ce3aec8"

S = "${WORKDIR}/git"

inherit distutils

DEPENDS_append = " xz"

RDEPENDS_${PN} += "python-core python-io python-pkgutil"
RDEPENDS_${PN}_class-native += "python-core"

BBCLASSEXTEND = "native"
