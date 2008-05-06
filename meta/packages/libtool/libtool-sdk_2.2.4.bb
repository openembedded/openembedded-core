require libtool.inc
require libtool_${PV}.bb

PR = "r0"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libtool-${PV}"
SRC_URI_append = " file://cross_compile.patch;patch=1"

S = "${WORKDIR}/libtool-${PV}"

inherit sdk

do_install () {
	install -d ${D}${bindir}/
	install -m 0755 libtool ${D}${bindir}/
	install -m 0755 libtoolize ${D}${bindir}/

	install -d ${D}${libdir}/
	oe_libinstall -a -so -C libltdl libltdl ${D}${libdir}

	install -d ${D}${includedir}/
	install -m 0644 libltdl/ltdl.h ${D}${includedir}

	install -d ${D}${datadir}/libtool/config/
	install -c ${S}/libltdl/config/config.guess ${D}${datadir}/libtool/
	install -c ${S}/libltdl/config/config.sub ${D}${datadir}/libtool/
	install -c -m 0644 ${S}/libltdl/config/ltmain.sh ${D}${datadir}/libtool/config/

	install -d ${D}${datadir}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/libtool.m4 ${D}${datadir}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltdl.m4 ${D}${datadir}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltoptions.m4 ${D}${datadir}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltversion.m4 ${D}${datadir}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltsugar.m4 ${D}${datadir}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/lt~obsolete.m4 ${D}${datadir}/aclocal/
}

do_stage () {
	install -d ${STAGING_BINDIR}/
	install -m 0755 libtool ${STAGING_BINDIR}/${HOST_SYS}-libtool
	install -m 0755 libtoolize ${STAGING_BINDIR}/libtoolize

	oe_libinstall -a -so -C libltdl libltdl ${STAGING_LIBDIR}
	install -m 0644 libltdl/ltdl.h ${STAGING_INCDIR}/

	install -d ${STAGING_DATADIR}/libtool/config/
	install -c ${S}/libltdl/config/config.guess ${STAGING_DATADIR}/libtool/
	install -c ${S}/libltdl/config/config.sub ${STAGING_DATADIR}/libtool/
	install -c -m 0644 ${S}/libltdl/config/ltmain.sh ${STAGING_DATADIR}/libtool/config/

	install -d ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/libtool.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltdl.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltoptions.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltversion.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltsugar.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/lt~obsolete.m4 ${STAGING_DATADIR}/aclocal/
}
