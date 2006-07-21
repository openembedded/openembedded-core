SECTION = "libs"
DESCRIPTION = "Jim Clarkes XML parser library."
HOMEPAGE = "http://expat.sourceforge.net/"
LICENSE = "MIT"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/expat/expat-${PV}.tar.gz \
	   file://autotools.patch;patch=1"
S = "${WORKDIR}/expat-${PV}"

inherit autotools lib_package
export LTCC = "${CC}"

do_configure () {
	rm -f ${S}/conftools/libtool.m4
	autotools_do_configure
}

do_stage () {
	install -m 0644 ${S}/lib/expat.h ${STAGING_INCDIR}/
	oe_libinstall -so libexpat ${STAGING_LIBDIR}
}

do_install () {
	oe_runmake prefix="${D}${prefix}" \
		bindir="${D}${bindir}" \
		libdir="${D}${libdir}" \
		includedir="${D}${includedir}" \
		install
}
