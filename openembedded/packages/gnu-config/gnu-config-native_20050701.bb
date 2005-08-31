SECTION = "base"
include gnu-config_${PV}.bb

inherit native

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gnu-config"

do_stage () {
	install -d ${STAGING_DATADIR}/gnu-config
	cat ${WORKDIR}/gnu-configize.in | \
		sed -e 's,@gnu-configdir@,${STAGING_DATADIR}/gnu-config,' \
		    -e 's,@autom4te_perllibdir@,${STAGING_DATADIR}/autoconf,' > ${STAGING_BINDIR}/gnu-configize
	chmod 755 ${STAGING_BINDIR}/gnu-configize
	install -m 0644 config.guess config.sub ${STAGING_DATADIR}/gnu-config/
}
