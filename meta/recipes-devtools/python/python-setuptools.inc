SUMMARY = "Download, build, install, upgrade, and uninstall Python packages"
HOMEPAGE = "https://pypi.python.org/pypi/setuptools"
SECTION = "devel/python"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://setup.py;beginline=146;endline=146;md5=3e8df024d6c1442d18e84acf8fbbc475"

SRCNAME = "setuptools"

SRC_URI = "https://files.pythonhosted.org/packages/source/s/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"

SRC_URI[md5sum] = "28ecfd0f2574b489b9a18343879a7324"
SRC_URI[sha256sum] = "b539118819a4857378398891fa5366e090690e46b3e41421a1e07d6e9fd8feb0"

UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/setuptools"

S = "${WORKDIR}/${SRCNAME}-${PV}"

do_install_prepend() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
}
