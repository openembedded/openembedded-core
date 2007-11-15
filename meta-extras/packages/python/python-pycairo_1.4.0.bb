DESCRIPTION = "Python Bindings for the Cairo canvas library"
SECTION = "python-devel"
HOMEPAGE = "http://cairographics.org/pycairo"
LICENSE = "LGPL MPL"
DEPENDS = "cairo"
PR = "ml0"

SRC_URI = "http://cairographics.org/releases/pycairo-${PV}.tar.gz"
S = "${WORKDIR}/pycairo-${PV}"

inherit distutils pkgconfig

do_stage() {
	install -d ${STAGING_INCDIR}
	install -m 0644 cairo/pycairo.h ${STAGING_INCDIR}
}
