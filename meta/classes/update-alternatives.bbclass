# This class is used to help the alternatives system which is useful when
# multiple sources provide same command. You can use update-alternatives
# command directly in your recipe, but in most cases this class simplifies
# that job.
#
# There are two basic modes supported: 'single update' and 'batch update'
#
# 'single update' is used for a single alternative command, and you're
# expected to provide at least below keywords:
#
#     ALTERNATIVE_NAME - the name that the alternative is registered
#     ALTERNATIVE_PATH - the path of installed alternative
#
# ALTERNATIVE_PRIORITY and ALTERNATIVE_LINK are optional which have defaults
# in this class.
#
# 'batch update' is used if you have multiple alternatives to be updated.
# Unlike 'single update', 'batch update' in most times only require two
# parameters:
#
#     ALTERNATIVE_LINKS - a list of symbolic links for which you'd like to
#                         create alternatives, with space as delimiter, e.g:
#
#         ALTERNATIVE_LINKS = "${bindir}/cmd1 ${sbindir}/cmd2 ..."
#
#     ALTERNATIVE_PRIORITY - optional, applies to all
#
# To simplify the design, this class has the assumption that for a name
# listed in ALTERNATIVE_LINKS, say /path/cmd:
#
#     the name of the alternative would be: cmd
#     the path of installed alternative would be: /path/cmd.${PN}
#     ${D}/path/cmd will be renamed to ${D}/path/cmd.{PN} automatically
#     priority will be the same from ALTERNATIVE_PRIORITY
#
# If above assumption breaks your requirement, then you still need to use
# your own update-alternatives command directly.

# defaults
ALTERNATIVE_PRIORITY = "10"
ALTERNATIVE_LINK = "${bindir}/${ALTERNATIVE_NAME}"

update_alternatives_postinst() {
update-alternatives --install ${ALTERNATIVE_LINK} ${ALTERNATIVE_NAME} ${ALTERNATIVE_PATH} ${ALTERNATIVE_PRIORITY}
}

update_alternatives_postrm() {
update-alternatives --remove ${ALTERNATIVE_NAME} ${ALTERNATIVE_PATH}
}

# for batch alternatives, we use a simple approach to require only one parameter
# with the rest of the info deduced implicitly
update_alternatives_batch_postinst() {
for link in ${ALTERNATIVE_LINKS}
do
	name=`basename ${link}`
	path=${link}.${PN}
	update-alternatives --install ${link} ${name} ${path} ${ALTERNATIVE_PRIORITY}
done
}

update_alternatives_batch_postrm() {
for link in ${ALTERNATIVE_LINKS}
do
	name=`basename ${link}`
	path=${link}.${PN}
	update-alternatives --remove ${name} $path
done
}

update_alternatives_batch_doinstall() {
	for link in ${ALTERNATIVE_LINKS}
	do
		mv ${D}${link} ${D}${link}.${PN}
	done
}

def update_alternatives_after_parse(d):
    if bb.data.inherits_class('native', d) or bb.data.inherits_class('nativesdk', d):
        return

    if d.getVar('ALTERNATIVE_LINKS') != None:
        doinstall = d.getVar('do_install', 0)
        doinstall += d.getVar('update_alternatives_batch_doinstall', 0)
        d.setVar('do_install', doinstall)
        return

    if d.getVar('ALTERNATIVE_NAME') == None:
        raise bb.build.FuncFailed, "%s inherits update-alternatives but doesn't set ALTERNATIVE_NAME" % d.getVar('FILE')
    if d.getVar('ALTERNATIVE_PATH') == None:
        raise bb.build.FuncFailed, "%s inherits update-alternatives but doesn't set ALTERNATIVE_PATH" % d.getVar('FILE')

python __anonymous() {
    update_alternatives_after_parse(d)
}

python populate_packages_prepend () {
	pkg = d.getVar('PN', True)
	bb.note('adding update-alternatives calls to postinst/postrm for %s' % pkg)
	postinst = d.getVar('pkg_postinst_%s' % pkg, True) or d.getVar('pkg_postinst', True)
	if not postinst:
		postinst = '#!/bin/sh\n'
	if d.getVar('ALTERNATIVE_LINKS') != None:
		postinst += d.getVar('update_alternatives_batch_postinst', True)
	else:
		postinst += d.getVar('update_alternatives_postinst', True)
	d.setVar('pkg_postinst_%s' % pkg, postinst)
	postrm = d.getVar('pkg_postrm_%s' % pkg, True) or d.getVar('pkg_postrm', True)
	if not postrm:
		postrm = '#!/bin/sh\n'
	if d.getVar('ALTERNATIVE_LINKS') != None:
		postrm += d.getVar('update_alternatives_batch_postrm', True)
	else:
		postrm += d.getVar('update_alternatives_postrm', True)
	d.setVar('pkg_postrm_%s' % pkg, postrm)
}
