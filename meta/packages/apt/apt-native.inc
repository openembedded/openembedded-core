require apt.inc
inherit native

DEPENDS += "dpkg-native gettext-native"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/apt-${PV}"
PACKAGES = ""
USE_NLS = "yes"

SRC_URI += "file://db_linking_hack.patch;patch=1"

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

	outdir = os.path.join(bb.data.getVar('sysconfdir', d, 1), 'apt')
	if not os.path.exists(outdir):
		os.makedirs(outdir)
	outpath = os.path.join(outdir, 'apt.conf.sample')

	outfile = file(outpath, 'w')
	outfile.write(data)
	outfile.close()
}

do_stage_base () {
	install -d ${bindir}
	install -m 0755 bin/apt-cdrom ${bindir}/
	install -m 0755 bin/apt-get ${bindir}/
	install -m 0755 bin/apt-config ${bindir}/
	install -m 0755 bin/apt-cache ${bindir}/
	install -m 0755 bin/apt-sortpkgs ${bindir}/
	install -m 0755 bin/apt-extracttemplates ${bindir}/

	eval `cat environment.mak | grep ^GLIBC_VER | sed -e's, = ,=,'`
	oe_libinstall -so -C bin libapt-pkg$GLIBC_VER-6 ${libdir}/
	ln -sf libapt-pkg$GLIBC_VER-6.so ${libdir}/libapt-pkg.so
	oe_libinstall -so -C bin libapt-inst$GLIBC_VER-6 ${libdir}/
	ln -sf libapt-inst$GLIBC_VER-6.so ${libdir}/libapt-inst.so

	install -d ${libdir}/apt/methods
	install -m 0755 bin/methods/* ${libdir}/apt/methods/

	install -d ${libdir}/dpkg/methods/apt
	install -m 0644 dselect/desc.apt ${libdir}/dpkg/methods/apt/ 
	install -m 0644 dselect/names ${libdir}/dpkg/methods/apt/ 
	install -m 0755 dselect/install ${libdir}/dpkg/methods/apt/ 
	install -m 0755 dselect/setup ${libdir}/dpkg/methods/apt/ 
	install -m 0755 dselect/update ${libdir}/dpkg/methods/apt/ 

	install -d ${sysconfdir}/apt
	install -d ${localstatedir}/lib/apt/lists/partial
	install -d ${localstatedir}/cache/apt/archives/partial

	install -d ${localstatedir}/log/apt/
}
