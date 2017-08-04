SUMMARY = "The PyPA recommended tool for installing Python packages"
HOMEPAGE = "https://pypi.python.org/pypi/pip"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=25fba45109565f87de20bae85bc39452"

DEPENDS += "python3 python3-setuptools-native"

SRC_URI[md5sum] = "35f01da33009719497f01a4ba69d63c9"
SRC_URI[sha256sum] = "09f243e1a7b461f654c26a725fa373211bb7ff17a9300058b205c61658ca940d"

inherit pypi distutils3

DISTUTILS_INSTALL_ARGS += "--install-lib=${D}${PYTHON_SITEPACKAGES_DIR}"

do_install_prepend() {
    install -d ${D}${PYTHON_SITEPACKAGES_DIR}
}

# Use setuptools site.py instead, avoid shared state issue
do_install_append() {
    rm ${D}${PYTHON_SITEPACKAGES_DIR}/site.py
    rm ${D}${PYTHON_SITEPACKAGES_DIR}/__pycache__/site.cpython-*.pyc

    # Install as pip3 and leave pip2 as default
    rm ${D}/${bindir}/pip

    # Installed eggs need to be passed directly to the interpreter via a pth file
    echo "./${PYPI_PACKAGE}-${PV}-py${PYTHON_BASEVERSION}.egg" > ${D}${PYTHON_SITEPACKAGES_DIR}/${PYPI_PACKAGE}-${PV}.pth

    # Make sure we use /usr/bin/env python3
    for PYTHSCRIPT in `grep -rIl ${bindir} ${D}${bindir}/pip3*`; do
        sed -i -e '1s|^#!.*|#!/usr/bin/env python3|' $PYTHSCRIPT
    done
}

RDEPENDS_${PN} = "\
  python3-compile \
  python3-io \
  python3-html \
  python3-json \
  python3-netserver \
  python3-setuptools \
  python3-unixadmin \
  python3-xmlrpc \
"

BBCLASSEXTEND = "native nativesdk"
