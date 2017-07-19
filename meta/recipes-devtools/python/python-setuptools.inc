SUMMARY = "Download, build, install, upgrade, and uninstall Python packages"
HOMEPAGE = "https://pypi.python.org/pypi/setuptools"
SECTION = "devel/python"
LICENSE = "MIT"

LIC_FILES_CHKSUM = "file://LICENSE;beginline=1;endline=19;md5=9a33897f1bca1160d7aad3835152e158"

SRCNAME = "setuptools"

SRC_URI = "https://files.pythonhosted.org/packages/source/s/${SRCNAME}/${SRCNAME}-${PV}.zip"

SRC_URI[md5sum] = "60df703040ad8024d24727dc95483740"
SRC_URI[sha256sum] = "4c2bda3829c9dbbe9c2b33d89c4b1cb6d45813615ab8e248ad352e3697a96d81"

UPSTREAM_CHECK_URI = "https://pypi.python.org/pypi/setuptools"

S = "${WORKDIR}/${SRCNAME}-${PV}"

do_install_prepend() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
}
