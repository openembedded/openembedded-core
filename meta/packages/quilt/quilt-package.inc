PACKAGES += "guards guards-doc"
FILES_${PN} = "${sysconfdir} ${datadir}/quilt \
	       ${bindir}/quilt ${libdir}/quilt"
FILES_guards = "${bindir}/guards"
FILES_${PN}-doc = "${mandir}/man1/quilt.1 ${docdir}/${P}"
FILES_guards-doc = "${mandir}/man1/guards.1"

do_install () {
	oe_runmake 'BUILD_ROOT=${D}' install
}
