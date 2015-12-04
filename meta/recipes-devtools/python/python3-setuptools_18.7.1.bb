SUMMARY = "Download, build, install, upgrade, and uninstall Python packages"
HOMEPAGE = "http://packages.python.org/setuptools"
SECTION = "devel/python"
LICENSE = "PSF"
LIC_FILES_CHKSUM = "file://setup.py;beginline=78;endline=78;md5=8a314270dd7a8dbca741775415f1716e"

SRCNAME = "setuptools"
DEPENDS += "python3"
DEPENDS_class-native += "python3-native"

SRC_URI = " \
  http://pypi.python.org/packages/source/s/${SRCNAME}/${SRCNAME}-${PV}.tar.gz \
"
SRC_URI[md5sum] = "a0984da9cd8d7b582e1fd7de67dfdbcc"
SRC_URI[sha256sum] = "aff36c95035e0b311eacb1434e3f7e85f5ccaad477773847e582978f8f45bd74"
UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/setuptools"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils3

DISTUTILS_INSTALL_ARGS += "--install-lib=${D}${libdir}/${PYTHON_DIR}/site-packages"

do_install_prepend() {
    install -d ${D}/${libdir}/${PYTHON_DIR}/site-packages
}
#
#  The installer puts the wrong path in the setuptools.pth file.  Correct it.
#
do_install_append() {
    rm ${D}${PYTHON_SITEPACKAGES_DIR}/setuptools.pth
    mv ${D}${bindir}/easy_install ${D}${bindir}/easy3_install
    echo "./${SRCNAME}-${PV}-py${PYTHON_BASEVERSION}.egg" > ${D}${PYTHON_SITEPACKAGES_DIR}/setuptools.pth
}

RDEPENDS_${PN} = "\
  python3-distutils \
  python3-compression \
"
RDEPENDS_${PN}_class-target = "\
  python3-ctypes \
  python3-distutils \
  python3-email \
  python3-importlib \
  python3-numbers \
  python3-compression \
  python3-shell \
  python3-subprocess \
  python3-textutils \
  python3-pkgutil \
  python3-threading \
  python3-misc \
  python3-unittest \
  python3-xml \
"
BBCLASSEXTEND = "native"
