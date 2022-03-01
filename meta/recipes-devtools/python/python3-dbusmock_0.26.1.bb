SUMMARY = "With this program/Python library you can easily create mock objects on D-Bus"
HOMEPAGE = "https://pypi.org/project/python-dbusmock/"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI[sha256sum] = "0e95ef6ec1e6b58aeb0e08839a76972debea94184570f09e29e5f12551399e55"

PYPI_PACKAGE = "python-dbusmock"
PIP_INSTALL_PACKAGE = "python_dbusmock"

inherit pypi setuptools3

RDEPENDS:${PN} += "\
    ${PYTHON_PN}-dbus \
    ${PYTHON_PN}-pygobject \
    ${PYTHON_PN}-unittest \
    ${PYTHON_PN}-xml \
    "
