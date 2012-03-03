UPDATERCPN ?= "${PN}"

DEPENDS_append = " update-rc.d-native"
UPDATERCD = "update-rc.d"
UPDATERCD_virtclass-cross = ""
UPDATERCD_virtclass-native = ""
UPDATERCD_virtclass-nativesdk = ""

RDEPENDS_${UPDATERCPN}_append = " ${UPDATERCD}"

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
    if d.getVar('INITSCRIPT_PACKAGES') == None:
        if d.getVar('INITSCRIPT_NAME') == None:
            raise bb.build.FuncFailed, "%s inherits update-rc.d but doesn't set INITSCRIPT_NAME" % d.getVar('FILE')
        if d.getVar('INITSCRIPT_PARAMS') == None:
            raise bb.build.FuncFailed, "%s inherits update-rc.d but doesn't set INITSCRIPT_PARAMS" % d.getVar('FILE')

python __anonymous() {
    update_rc_after_parse(d)
}

python populate_packages_prepend () {
	def update_rcd_package(pkg):
		bb.debug(1, 'adding update-rc.d calls to postinst/postrm for %s' % pkg)
		localdata = bb.data.createCopy(d)
		overrides = localdata.getVar("OVERRIDES", True)
		localdata.setVar("OVERRIDES", "%s:%s" % (pkg, overrides))
		bb.data.update_data(localdata)

		"""
		update_rc.d postinst is appended here because pkg_postinst may require to
		execute on the target. Not doing so may cause update_rc.d postinst invoked
		twice to cause unwanted warnings.
		""" 
		postinst = localdata.getVar('pkg_postinst', True)
		if not postinst:
			postinst = '#!/bin/sh\n'
		postinst += localdata.getVar('updatercd_postinst', True)
		d.setVar('pkg_postinst_%s' % pkg, postinst)

		prerm = localdata.getVar('pkg_prerm', True)
		if not prerm:
			prerm = '#!/bin/sh\n'
		prerm += localdata.getVar('updatercd_prerm', True)
		d.setVar('pkg_prerm_%s' % pkg, prerm)

	        postrm = localdata.getVar('pkg_postrm', True)
	        if not postrm:
	                postrm = '#!/bin/sh\n'
                postrm += localdata.getVar('updatercd_postrm', True)
		d.setVar('pkg_postrm_%s' % pkg, postrm)

	pkgs = d.getVar('INITSCRIPT_PACKAGES', True)
	if pkgs == None:
		pkgs = d.getVar('UPDATERCPN', True)
		packages = (d.getVar('PACKAGES', True) or "").split()
		if not pkgs in packages and packages != []:
			pkgs = packages[0]
	for pkg in pkgs.split():
		update_rcd_package(pkg)
}
