DESCRIPTION = "Download, build, install, upgrade, and uninstall Python packages"
HOMEPAGE = "http://cheeseshop.python.org/pypi/setuptools"
SECTION = "devel/python"
LICENSE = "PSF"
LIC_FILES_CHKSUM = "file://setup.py;beginline=107;endline=107;md5=8a314270dd7a8dbca741775415f1716e"

SRCNAME = "setuptools"
DEPENDS += "python"
DEPENDS_class-native += "python-native"

SRC_URI = "\
  http://cheeseshop.python.org/packages/source/s/setuptools/${SRCNAME}-${PV}.tar.gz\
"
S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils

DISTUTILS_INSTALL_ARGS += "--install-lib=${D}${libdir}/${PYTHON_DIR}/site-packages"

do_install_prepend() {
    install -d ${D}/${libdir}/${PYTHON_DIR}/site-packages
}

RDEPENDS_${PN} = "\
  python-stringold \
  python-email \
  python-shell \
  python-distutils \
  python-compression \
"

RDEPENDS_${PN}_class-native = "\
  python-distutils \
  python-compression \
"

SRC_URI[md5sum] = "1f33594d25d574a1640ffb84667c6759"
SRC_URI[sha256sum] = "b35e3d7c79cfdb5b38cfc71f4b0deab4350c1176fc4bb05bfa8945504ecfb028"

BBCLASSEXTEND = "native"
