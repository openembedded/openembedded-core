UPDATERCPN ?= "${PN}"

DEPENDS_append = " update-rc.d-native"
RDEPENDS_${UPDATERCPN}_append = " update-rc.d"

INITSCRIPT_PARAMS ?= "defaults"

INIT_D_DIR = "${sysconfdir}/init.d"

updatercd_postinst() {
if test "x$D" != "x"; then
	OPT="-r $D"
else
	OPT="-s"
fi
update-rc.d $OPT ${INITSCRIPT_NAME} ${INITSCRIPT_PARAMS}
}

updatercd_prerm() {
if test "x$D" = "x"; then
	${INIT_D_DIR}/${INITSCRIPT_NAME} stop
fi
}

updatercd_postrm() {
update-rc.d $D ${INITSCRIPT_NAME} remove
}


def update_rc_after_parse(d):
    if bb.data.getVar('INITSCRIPT_PACKAGES', d) == None:
        if bb.data.getVar('INITSCRIPT_NAME', d) == None:
            raise bb.build.FuncFailed, "%s inherits update-rc.d but doesn't set INITSCRIPT_NAME" % bb.data.getVar('FILE', d)
        if bb.data.getVar('INITSCRIPT_PARAMS', d) == None:
            raise bb.build.FuncFailed, "%s inherits update-rc.d but doesn't set INITSCRIPT_PARAMS" % bb.data.getVar('FILE', d)

python __anonymous() {
    update_rc_after_parse(d)
}

python populate_packages_prepend () {
	def update_rcd_package(pkg):
		bb.debug(1, 'adding update-rc.d calls to postinst/postrm for %s' % pkg)
		localdata = bb.data.createCopy(d)
		overrides = bb.data.getVar("OVERRIDES", localdata, 1)
		bb.data.setVar("OVERRIDES", "%s:%s" % (pkg, overrides), localdata)
		bb.data.update_data(localdata)

		"""
		update_rc.d postinst is appended here because pkg_postinst may require to
		execute on the target. Not doing so may cause update_rc.d postinst invoked
		twice to cause unwanted warnings.
		""" 
		postinst = bb.data.getVar('pkg_postinst', localdata, 1)
		if not postinst:
			postinst = '#!/bin/sh\n'
		postinst += bb.data.getVar('updatercd_postinst', localdata, 1)
		bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)

		prerm = bb.data.getVar('pkg_prerm', localdata, 1)
		if not prerm:
			prerm = '#!/bin/sh\n'
		prerm += bb.data.getVar('updatercd_prerm', localdata, 1)
		bb.data.setVar('pkg_prerm_%s' % pkg, prerm, d)

	        postrm = bb.data.getVar('pkg_postrm', localdata, 1)
	        if not postrm:
	                postrm = '#!/bin/sh\n'
                postrm += bb.data.getVar('updatercd_postrm', localdata, 1)
		bb.data.setVar('pkg_postrm_%s' % pkg, postrm, d)

	pkgs = bb.data.getVar('INITSCRIPT_PACKAGES', d, 1)
	if pkgs == None:
		pkgs = bb.data.getVar('UPDATERCPN', d, 1)
		packages = (bb.data.getVar('PACKAGES', d, 1) or "").split()
		if not pkgs in packages and packages != []:
			pkgs = packages[0]
	for pkg in pkgs.split():
		update_rcd_package(pkg)
}
