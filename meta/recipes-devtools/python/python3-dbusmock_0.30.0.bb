SUMMARY = "With this program/Python library you can easily create mock objects on D-Bus"
HOMEPAGE = "https://pypi.org/project/python-dbusmock/"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=e6a600fd5e1d9cbde2d983680233ad02"

SRC_URI[sha256sum] = "dbb59e715b4d88089caed950edf93c46cb5f022ceae5d8ae37064b73baf956c1"

PYPI_PACKAGE = "python-dbusmock"

inherit pypi python_setuptools_build_meta
DEPENDS += "python3-setuptools-scm-native"

RDEPENDS:${PN} += "\
    ${PYTHON_PN}-dbus \
    ${PYTHON_PN}-unittest \
    ${PYTHON_PN}-xml \
    "

RRECOMMENDS:${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'gobject-introspection-data', '${MLPREFIX}${PYTHON_PN}-pygobject', '', d)}"

BBCLASSEXTEND = "native"
