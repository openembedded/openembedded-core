SUMMARY = "Accessing and Modifying INI files"
HOMEPAGE = "https://pypi.python.org/pypi/iniparse/"
LICENSE = "MIT & PSF"
LIC_FILES_CHKSUM = "file://LICENSE-PSF;md5=1c78a5bb3584b353496d5f6f34edb4b2 \
                    file://LICENSE;md5=52f28065af11d69382693b45b5a8eb54"

SRC_URI = "https://files.pythonhosted.org/packages/source/i/iniparse/iniparse-${PV}.tar.gz"
SRC_URI[md5sum] = "5e573e9e9733d97623881ce9bbe5eca6"
SRC_URI[sha256sum] = "abc1ee12d2cfb2506109072d6c21e40b6c75a3fe90a9c924327d80bc0d99c054"

inherit distutils

RDEPENDS_${PN} += "python-core"

BBCLASSEXTEND = "native nativesdk"

S = "${WORKDIR}/iniparse-${PV}"
