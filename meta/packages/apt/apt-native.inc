require apt.inc
inherit native

DEPENDS += "dpkg-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/apt-${PV}"
PACKAGES = ""
USE_NLS = "no"

python do_stage () {
	bb.build.exec_func('do_stage_base', d)
	bb.build.exec_func('do_stage_config', d)
}

python do_stage_config () {
	indir = os.path.dirname(bb.data.getVar('FILE',d,1))
	infile = file(os.path.join(indir, 'files', 'apt.conf'), 'r')
	data = infile.read()
	infile.close()

	data = bb.data.expand(data, d)

	outpath = bb.data.expand('${STAGING_DIR}/${sysconfdir}/apt.conf', d)
	outfile = file(outpath, 'w')
	outfile.write(data)
	outfile.close()
}

do_stage_base () {
	install -d ${STAGING_BINDIR}
	install -m 0755 bin/apt-cdrom ${STAGING_BINDIR}/
	install -m 0755 bin/apt-get ${STAGING_BINDIR}/
	install -m 0755 bin/apt-config ${STAGING_BINDIR}/
	install -m 0755 bin/apt-cache ${STAGING_BINDIR}/

	install -m 0755 bin/apt-sortpkgs ${STAGING_BINDIR}/
	install -m 0755 bin/apt-extracttemplates ${STAGING_BINDIR}/

	eval `cat environment.mak | grep ^GLIBC_VER | sed -e's, = ,=,'`
	oe_libinstall -so -C bin libapt-pkg$GLIBC_VER-6 ${STAGING_LIBDIR}/
	ln -sf libapt-pkg$GLIBC_VER-6.so ${STAGING_LIBDIR}/libapt-pkg.so
	oe_libinstall -so -C bin libapt-inst$GLIBC_VER-6 ${STAGING_LIBDIR}/
	ln -sf libapt-inst$GLIBC_VER-6.so ${STAGING_LIBDIR}/libapt-inst.so

	install -d ${STAGING_LIBDIR}/apt/methods
	install -m 0755 bin/methods/* ${STAGING_LIBDIR}/apt/methods/

	install -d ${STAGING_LIBDIR}/dpkg/methods/apt
	install -m 0644 dselect/desc.apt ${STAGING_LIBDIR}/dpkg/methods/apt/ 
	install -m 0644 dselect/names ${STAGING_LIBDIR}/dpkg/methods/apt/ 
	install -m 0755 dselect/install ${STAGING_LIBDIR}/dpkg/methods/apt/ 
	install -m 0755 dselect/setup ${STAGING_LIBDIR}/dpkg/methods/apt/ 
	install -m 0755 dselect/update ${STAGING_LIBDIR}/dpkg/methods/apt/ 

	install -d ${STAGING_DIR}${sysconfdir}/apt
	install -d ${STAGING_DIR}${localstatedir}/lib/apt/lists/partial
	install -d ${STAGING_DIR}${localstatedir}/cache/apt/archives/partial
}
