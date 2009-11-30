DESCRIPTION = "Python Bindings for the Cairo canvas library"
SECTION = "python-devel"
HOMEPAGE = "http://cairographics.org/pycairo"
LICENSE = "LGPL MPL"
DEPENDS = "cairo"
PR = "ml1.3"

SRC_URI = "http://cairographics.org/releases/pycairo-${PV}.tar.gz"
S = "${WORKDIR}/pycairo-${PV}"

inherit distutils pkgconfig

do_install_append () {
	install -d ${D}${includedir}
	install -d ${D}${libdir}
	mv ${D}${datadir}/include/* ${D}${includedir}
	mv ${D}${datadir}/lib/* ${D}${libdir}
	sed -i -e 's#prefix=.*#prefix=${prefix}#' ${D}${libdir}/pkgconfig/pycairo.pc
}
