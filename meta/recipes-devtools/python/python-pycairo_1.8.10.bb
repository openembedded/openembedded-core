DESCRIPTION = "Python Bindings for the Cairo canvas library"
HOMEPAGE = "http://cairographics.org/pycairo"
BUGTRACKER = "http://bugs.freedesktop.org"
SECTION = "python-devel"
LICENSE = "LGPLv2.1 & MPL-1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f2e071ab72978431b294a0d696327421"
# cairo >= 1.8.8
DEPENDS = "cairo"
PR = "r1"

SRC_URI = "http://cairographics.org/releases/py2cairo-${PV}.tar.gz"

SRC_URI[md5sum] = "87421a6a70304120555ba7ba238f3dc3"
SRC_URI[sha256sum] = "b15f71019e42e06d86f7e8fe5587f07c3de5a59a6c3a071b25fe100796dbcd56"
S = "${WORKDIR}/pycairo-${PV}"

inherit distutils pkgconfig

do_compile_prepend() {
#fix the installation path of __init__.py
#It was going in the sysroot instead of target install location
	sed -i -e "s#dsy.get_python_lib()#'${D}${PYTHON_SITEPACKAGES_DIR}'#" ${S}/setup.py
}

do_install_append () {
	install -d ${D}${includedir}
	install -d ${D}${libdir}
	mv ${D}${datadir}/include/* ${D}${includedir}
	mv ${D}${datadir}/lib/* ${D}${libdir}
	sed -i -e 's#prefix=.*#prefix=${prefix}#' ${D}${libdir}/pkgconfig/pycairo.pc
}
