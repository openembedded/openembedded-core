SUMMARY = "Download, build, install, upgrade, and uninstall Python packages"
HOMEPAGE = "http://packages.python.org/distribute"
SECTION = "devel/python"
LICENSE = "PSF"
LIC_FILES_CHKSUM = "file://setup.py;beginline=234;endline=234;md5=26f6b02022b737126d3c88838782dddb"

SRCNAME = "distribute"
PR = "ml5"
DEPENDS += "python"
DEPENDS_class-native += "python-native"

SRC_URI = " \
  http://pypi.python.org/packages/source/d/distribute/${SRCNAME}-${PV}.tar.gz \
"
SRC_URI[md5sum] = "acb7a2da81e3612bfb1608abe4f0e568"
SRC_URI[sha256sum] = "8970cd1e148b5d1fea9430584aea66c45ea22d80e0933393ec49ebc388f718df"

S = "${WORKDIR}/${SRCNAME}-${PV}"

#  http://python-distribute.org/distribute_setup.py 

# force the selection of python3
#PYTHON_BASEVERSION = "3.3"
#PYTHON_MAJMIN = "3.3"

inherit distutils

DISTUTILS_INSTALL_ARGS += "--install-lib=${D}${libdir}/${PYTHON_DIR}/site-packages"

do_install_prepend() {
    install -d ${D}/${libdir}/${PYTHON_DIR}/site-packages
}

#
#  The installer puts the wrong path in the setuptools.pth file.  Correct it.
#
do_install_append() {
    rm ${D}${PYTHON_SITEPACKAGES_DIR}/setuptools.pth
    echo "./${SRCNAME}-${PV}-py${PYTHON_BASEVERSION}.egg" > ${D}${PYTHON_SITEPACKAGES_DIR}/setuptools.pth
}

do_install_append_class-nativesdk() {
	sed -i -e '1s|^#!.*|#!/usr/bin/env python|' ${D}${PYTHON_SITEPACKAGES_DIR}/distribute-${PV}-py${PYTHON_BASEVERSION}.egg/setuptools/tests/test_resources.py
	sed -i -e '1s|^#!.*|#!/usr/bin/env python|' ${D}${bindir}/easy_install
	sed -i -e '1s|^#!.*|#!/usr/bin/env python|' ${D}${bindir}/easy_install-${PYTHON_BASEVERSION}
}

RDEPENDS_${PN} = "\
  python-distutils \
  python-compression \
"

RPROVIDES_${PN} += "python-setuptools"

BBCLASSEXTEND = "native nativesdk"
