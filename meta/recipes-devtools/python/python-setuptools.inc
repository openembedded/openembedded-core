SUMMARY = "Download, build, install, upgrade, and uninstall Python packages"
HOMEPAGE = "https://pypi.python.org/pypi/setuptools"
SECTION = "devel/python"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://setup.py;beginline=146;endline=146;md5=3e8df024d6c1442d18e84acf8fbbc475"

SRCNAME = "setuptools"

SRC_URI = "https://files.pythonhosted.org/packages/source/s/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"

SRC_URI[md5sum] = "66da89eeb1f33c2b947f316cfb3d7c7e"
SRC_URI[sha256sum] = "0818cc0de692c3a5c83ca83aa7ec7ba6bc206f278735f1e0267b8d0e095cfe7a"

UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/setuptools"

S = "${WORKDIR}/${SRCNAME}-${PV}"

do_install_prepend() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
}
