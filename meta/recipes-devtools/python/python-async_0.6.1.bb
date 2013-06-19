SUMMARY = "Python framework to process interdependent tasks in a pool of workers"
HOMEPAGE = "http://github.com/gitpython-developers/async"
SECTION = "devel/python"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://PKG-INFO;beginline=8;endline=8;md5=88df8e78b9edfd744953862179f2d14e"

SRC_URI = "http://pypi.python.org/packages/source/a/async/async-${PV}.tar.gz"
SRC_URI[md5sum] = "6f0e2ced1fe85f8410b9bde11be08587"
SRC_URI[sha256sum] = "41d14cc0456e03f34d13af284f65821d07d05c20e621bcaebd38f9ab5095d5d1"

S = "${WORKDIR}/async-${PV}"

inherit distutils

RDEPENDS_${PN} += "python-threading python-lang"

BBCLASSEXTEND = "nativesdk"
