do_install() {
	oe_runmake install_root=${D} install
	for r in ${rpcsvc}; do
		h=`echo $r|sed -e's,\.x$,.h,'`
		install -m 0644 ${S}/sunrpc/rpcsvc/$h ${D}/${includedir}/rpcsvc/
	done
	install -m 0644 ${WORKDIR}/etc/ld.so.conf ${D}/${sysconfdir}/
	install -d ${D}${libdir}/locale
	make -f ${WORKDIR}/generate-supported.mk IN="${S}/localedata/SUPPORTED" OUT="${WORKDIR}/SUPPORTED"
	# get rid of some broken files...
	for i in ${GLIBC_BROKEN_LOCALES}; do
		grep -v $i ${WORKDIR}/SUPPORTED > ${WORKDIR}/SUPPORTED.tmp
		mv ${WORKDIR}/SUPPORTED.tmp ${WORKDIR}/SUPPORTED
	done
	rm -f ${D}${sysconfdir}/rpc
	rm -rf ${D}${datadir}/zoneinfo
	rm -rf ${D}${libexecdir}/getconf
}

def get_libc_fpu_setting(bb, d):
    if d.getVar('TARGET_FPU', True) in [ 'soft' ]:
        return "--without-fp"
    return ""

python populate_packages_prepend () {
	if d.getVar('DEBIAN_NAMES', True):
		bpn = d.getVar('BPN', True)
		d.setVar('PKG_'+bpn, 'libc6')
		d.setVar('PKG_'+bpn+'-dev', 'libc6-dev')
		d.setVar('PKG_'+bpn+'-dbg', 'libc6-dbg')
		# For backward compatibility with old -dbg package
		d.setVar('RPROVIDES_' + bpn + '-dbg', 'libc-dbg')
		d.setVar('RCONFLICTS_' + bpn + '-dbg', 'libc-dbg')
		d.setVar('RREPLACES_' + bpn + '-dbg', 'libc-dbg')
}
