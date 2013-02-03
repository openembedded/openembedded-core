UPDATERCPN ?= "${PN}"

DEPENDS_append = " update-rc.d-native"
UPDATERCD = "update-rc.d"
UPDATERCD_virtclass-cross = ""
UPDATERCD_class-native = ""
UPDATERCD_class-nativesdk = ""

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
if [ "$D" != "" ]; then
	update-rc.d -f -r $D ${INITSCRIPT_NAME} remove
else
	update-rc.d ${INITSCRIPT_NAME} remove
fi
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

PACKAGESPLITFUNCS_prepend = "populate_packages_updatercd "

python populate_packages_updatercd () {
    def update_rcd_package(pkg):
        bb.debug(1, 'adding update-rc.d calls to postinst/postrm for %s' % pkg)
        """
        update_rc.d postinst is appended here because pkg_postinst may require to
        execute on the target. Not doing so may cause update_rc.d postinst invoked
        twice to cause unwanted warnings.
        """ 
        postinst = d.getVar('pkg_postinst_%s' % pkg, True)
        if not postinst:
            postinst = '#!/bin/sh\n'
        postinst += d.getVar('updatercd_postinst', True)
        d.setVar('pkg_postinst_%s' % pkg, postinst)

        prerm = d.getVar('pkg_prerm_%s' % pkg, True)
        if not prerm:
            prerm = '#!/bin/sh\n'
        prerm += d.getVar('updatercd_prerm', True)
        d.setVar('pkg_prerm_%s' % pkg, prerm)

        postrm = d.getVar('pkg_postrm_%s' % pkg, True)
        if not postrm:
                postrm = '#!/bin/sh\n'
        postrm += d.getVar('updatercd_postrm', True)
        d.setVar('pkg_postrm_%s' % pkg, postrm)

    # If the systemd class has also been inherited, then don't do anything as
    # the systemd units will override anything created by update-rc.d.
    if not d.getVar("SYSTEMD_BBCLASS_ENABLED", True):
        pkgs = d.getVar('INITSCRIPT_PACKAGES', True)
        if pkgs == None:
            pkgs = d.getVar('UPDATERCPN', True)
            packages = (d.getVar('PACKAGES', True) or "").split()
            if not pkgs in packages and packages != []:
                pkgs = packages[0]
        for pkg in pkgs.split():
            update_rcd_package(pkg)
}
