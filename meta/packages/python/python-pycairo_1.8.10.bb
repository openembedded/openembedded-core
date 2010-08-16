DESCRIPTION = "Python Bindings for the Cairo canvas library"
HOMEPAGE = "http://cairographics.org/pycairo"
BUGTRACKER = "http://bugs.freedesktop.org"
SECTION = "python-devel"
LICENSE = "LGPLv2.1 & MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=f2e071ab72978431b294a0d696327421"
# cairo >= 1.8.8
DEPENDS = "cairo"
PR = "ml0"

SRC_URI = "http://cairographics.org/releases/py2cairo-${PV}.tar.gz"
S = "${WORKDIR}/pycairo-${PV}"

inherit distutils pkgconfig

do_install_append () {
	install -d ${D}${includedir}
	install -d ${D}${libdir}
	mv ${D}${datadir}/include/* ${D}${includedir}
	mv ${D}${datadir}/lib/* ${D}${libdir}
	sed -i -e 's#prefix=.*#prefix=${prefix}#' ${D}${libdir}/pkgconfig/pycairo.pc
}
