require libtool.inc
require libtool_${PV}.bb

PR = "r18"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libtool-${PV}"
SRC_URI_append = " file://cross_compile.patch;patch=1 \
                   file://prefix.patch;patch=1"

S = "${WORKDIR}/libtool-${PV}"

inherit native

do_configure_prepend () {
	# Remove any existing libtool m4 since old stale versions would break
	# any upgrade
	rm -f ${STAGING_DATADIR}/aclocal/libtool.m4
	rm -f ${STAGING_DATADIR}/aclocal/lt*.m4
}

do_stage () {
	install -m 0755 ${HOST_SYS}-libtool ${STAGING_BINDIR}/${HOST_SYS}-libtool
	install -m 0755 libtoolize ${STAGING_BINDIR}/libtoolize
	oe_libinstall -a -so -C libltdl libltdl ${STAGING_LIBDIR}
	install -m 0644 libltdl/ltdl.h ${STAGING_INCDIR}/
	install -d ${STAGING_DATADIR}/libtool/config/ ${STAGING_DATADIR}/aclocal/
	install -c ${S}/libltdl/config/config.guess ${STAGING_DATADIR}/libtool/
	install -c ${S}/libltdl/config/config.sub ${STAGING_DATADIR}/libtool/
	install -c -m 0644 ${S}/libltdl/config/ltmain.sh ${STAGING_DATADIR}/libtool/config/
	install -c -m 0644 ${S}/libltdl/m4/libtool.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltdl.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltoptions.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltversion.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/ltsugar.m4 ${STAGING_DATADIR}/aclocal/
	install -c -m 0644 ${S}/libltdl/m4/lt~obsolete.m4 ${STAGING_DATADIR}/aclocal/
}

do_install () {
	:
}
