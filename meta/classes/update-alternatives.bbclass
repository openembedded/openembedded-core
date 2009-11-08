# defaults
ALTERNATIVE_PRIORITY = "10"
ALTERNATIVE_LINK = "${bindir}/${ALTERNATIVE_NAME}"

update_alternatives_postinst() {
update-alternatives --install ${ALTERNATIVE_LINK} ${ALTERNATIVE_NAME} ${ALTERNATIVE_PATH} ${ALTERNATIVE_PRIORITY}
}

update_alternatives_postrm() {
update-alternatives --remove ${ALTERNATIVE_NAME} ${ALTERNATIVE_PATH}
}

def update_alternatives_after_parse(d):
    if bb.data.getVar('ALTERNATIVE_NAME', d) == None:
        raise bb.build.FuncFailed, "%s inherits update-alternatives but doesn't set ALTERNATIVE_NAME" % bb.data.getVar('FILE', d)
    if bb.data.getVar('ALTERNATIVE_PATH', d) == None:
        raise bb.build.FuncFailed, "%s inherits update-alternatives but doesn't set ALTERNATIVE_PATH" % bb.data.getVar('FILE', d)

python __anonymous() {
    update_alternatives_after_parse(d)
}

python populate_packages_prepend () {
	pkg = bb.data.getVar('PN', d, 1)
	bb.note('adding update-alternatives calls to postinst/postrm for %s' % pkg)
	postinst = bb.data.getVar('pkg_postinst_%s' % pkg, d, 1) or bb.data.getVar('pkg_postinst', d, 1)
	if not postinst:
		postinst = '#!/bin/sh\n'
	postinst += bb.data.getVar('update_alternatives_postinst', d, 1)
	bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)
	postrm = bb.data.getVar('pkg_postrm_%s' % pkg, d, 1) or bb.data.getVar('pkg_postrm', d, 1)
	if not postrm:
		postrm = '#!/bin/sh\n'
	postrm += bb.data.getVar('update_alternatives_postrm', d, 1)
	bb.data.setVar('pkg_postrm_%s' % pkg, postrm, d)
}
