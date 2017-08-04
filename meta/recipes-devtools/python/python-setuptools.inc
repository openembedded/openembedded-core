SUMMARY = "Download, build, install, upgrade, and uninstall Python packages"
HOMEPAGE = "https://pypi.python.org/pypi/setuptools"
SECTION = "devel/python"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://LICENSE;beginline=1;endline=19;md5=9a33897f1bca1160d7aad3835152e158"

PYPI_PACKAGE_EXT = "zip"

inherit pypi

SRC_URI[md5sum] = "abfd02fba07b381c3a9682a32d765cc6"
SRC_URI[sha256sum] = "b080f276cc868670540b2c03cee06cc14d2faf9da7bec0f15058d1b402c94507"

DEPENDS += "${PYTHON_PN}"
DEPENDS_class-native += "${PYTHON_PN}-native"
DEPENDS_class-nativesdk += "nativesdk-${PYTHON_PN}"

DISTUTILS_INSTALL_ARGS += "--install-lib=${D}${PYTHON_SITEPACKAGES_DIR} \
                           --script-dir=${bindir}"

RDEPENDS_${PN}_class-native = "\
  ${PYTHON_PN}-distutils \
  ${PYTHON_PN}-compression \
"
RDEPENDS_${PN} = "\
  ${PYTHON_PN}-compile \
  ${PYTHON_PN}-compression \
  ${PYTHON_PN}-ctypes \
  ${PYTHON_PN}-distutils \
  ${PYTHON_PN}-email \
  ${PYTHON_PN}-html \
  ${PYTHON_PN}-netserver \
  ${PYTHON_PN}-numbers \
  ${PYTHON_PN}-pkgutil \
  ${PYTHON_PN}-plistlib \
  ${PYTHON_PN}-shell \
  ${PYTHON_PN}-stringold \
  ${PYTHON_PN}-threading \
  ${PYTHON_PN}-unittest \
  ${PYTHON_PN}-xml \
"

do_install_prepend() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
}

BBCLASSEXTEND = "native nativesdk"
