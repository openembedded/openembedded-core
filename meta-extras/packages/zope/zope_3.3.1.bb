DESCRIPTION = "A full fledged pluggable content management system with integrated web server and much more."
SECTION = "console/network"
PRIORITY = "optional"
DEPENDS = "python"
RDEPENDS = "python-core python-shell"
LICENSE = "ZPL"
PR = "r3"

SRC_URI = "http://www.zope.org/Products/Zope3/${PV}/Zope-${PV}.tgz"

S = "${WORKDIR}/Zope-${PV}"

do_configure() {
	./configure --with-python=${STAGING_BINDIR_NATIVE}/python --prefix=${prefix} --force
}

do_compile() {
	oe_runmake HOST_SYS=${HOST_SYS} BUILD_SYS=${BUILD_SYS} STAGING_INCDIR=${STAGING_INCDIR} STAGING_LIBDIR=${STAGING_LIBDIR}
}

PYTHON_MAJMIN = "2.5"

do_install() {
	install -d ${D}${libdir}/python${PYTHON_MAJMIN}
	oe_runmake install prefix=${D}${prefix} HOST_SYS=${HOST_SYS} BUILD_SYS=${BUILD_SYS} STAGING_INCDIR=${STAGING_INCDIR} STAGING_LIBDIR=${STAGING_LIBDIR}
	mv ${D}${libdir}/python/* ${D}${libdir}/python${PYTHON_MAJMIN} 
}

PACKAGES =+ "python-zopeinterface python-zopeinterface-dbg"

FILES_${PN} = "${prefix}"
FILES_${PN}_doc = "${prefix}/doc"
FILES_${PN}-dbg += "\
${libdir}/python${PYTHON_MAJMIN}/BTrees/.debug \
${libdir}/python${PYTHON_MAJMIN}/persistent/.debug \
${libdir}/python${PYTHON_MAJMIN}/zope/proxy/.debug \
${libdir}/python${PYTHON_MAJMIN}/zope/thread/.debug \
${libdir}/python${PYTHON_MAJMIN}/zope/security/.debug \
${libdir}/python${PYTHON_MAJMIN}/zope/hookable/.debug \
${libdir}/python${PYTHON_MAJMIN}/zope/app/container/.debug \
${libdir}/python${PYTHON_MAJMIN}/zope/i18nmessageid/.debug \
${libdir}/python${PYTHON_MAJMIN}/ZODB/.debug"
FILES_python-zopeinterface-dbg += "${libdir}/python${PYTHON_MAJMIN}/zope/interface/.debug "

FILES_python-zopeinterface = "${libdir}/python${PYTHON_MAJMIN}/zope/interface/*.* ${libdir}/python${PYTHON_MAJMIN}/zope/interface/common"
