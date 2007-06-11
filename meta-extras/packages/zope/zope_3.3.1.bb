DESCRIPTION = "A full fledged pluggable content management system with integrated web server and much more."
SECTION = "console/network"
PRIORITY = "optional"
DEPENDS = "python"
RDEPENDS = "python-core python-shell"
LICENSE = "ZPL"
PR = "r1"

SRC_URI = "http://www.zope.org/Products/Zope3/${PV}/Zope-${PV}.tgz"

S = "${WORKDIR}/Zope-${PV}"

do_configure() {
	./configure --with-python=${STAGING_BINDIR_NATIVE}/python --prefix=${prefix} --force
}

do_compile() {
	oe_runmake HOST_SYS=${HOST_SYS} BUILD_SYS=${BUILD_SYS}
}

do_install() {
	oe_runmake install prefix=${D}${prefix} HOST_SYS=${HOST_SYS} BUILD_SYS=${BUILD_SYS}
}

PACKAGES =+ "python-zopeinterface"

FILES_${PN} = "${prefix}"
FILES_${PN}_doc = "${prefix}/doc"
FILES_python-zopeinterface = "${libdir}/python/zope/interface/*.* ${libdir}/python/zope/interface/common"
