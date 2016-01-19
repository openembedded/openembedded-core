SUMMARY = "Downloads, builds, installs, upgrades, and uninstalls Python packages"
HOMEPAGE = "https://pypi.python.org/pypi/setuptools"
SECTION = "devel/python"
LICENSE = "Python-2.0 | ZPL-2.0"
LIC_FILES_CHKSUM = "file://setup.py;beginline=78;endline=78;md5=8a314270dd7a8dbca741775415f1716e"

SRCNAME = "setuptools"

PROVIDES = "python-distribute"

DEPENDS += "python"
DEPENDS_class-native += "python-native"

inherit distutils

SRC_URI = "https://pypi.python.org/packages/source/s/setuptools/setuptools-${PV}.tar.gz"
SRC_URI[md5sum] = "78353b1f80375ca5e088f4b4627ffe03"
SRC_URI[sha256sum] = "f90ed8eb70b14b0594ba74e9de4ffca040c0ec8ee505cbf3570499467859f71a"
UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/setuptools"

S = "${WORKDIR}/${SRCNAME}-${PV}"


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
  python-pkgutil \
  python-plistlib \
  python-numbers \
  python-html \
  python-netserver \
  python-ctypes \
  python-subprocess \
  python-unittest \
"

RDEPENDS_${PN}_class-native = "\
  python-distutils \
  python-compression \
"

RREPLACES_${PN} = "python-distribute"
RPROVIDES_${PN} = "python-distribute"
RCONFLICTS_${PN} = "python-distribute"

BBCLASSEXTEND = "native nativesdk"
