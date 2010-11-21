# This class is used to help the alternatives system which is useful when
# multiple sources provide same command. You can use update-alternatives
# command directly in your recipe, but in most cases this class simplifies
# that job.
#
# There're two basic modes supported: 'single update' and 'batch update'
#
# 'single update' is used for a single alternative command, and you're
# expected to provide at least below keywords:
#
#     ALTERNATIVE_NAME - the name that the alternative is registered
#     ALTERNATIVE_PATH - the path of installed alternative
#
# ALTENATIVE_PRIORITY and ALTERNATIVE_LINK are optional which have defautls
# in this class.
#
# 'batch update' is used if you have multiple alternatives to be updated.
# Unlike 'single update', 'batch update' in most times only require two
# parameter:
#
#     ALTERNATIVE_LINKS - a list of symbol links for which you'd like to
#                         create alternatives, with space as delimiter, e.g:
#
#         ALTERNATIVE_LINKS = "${bindir}/cmd1 ${sbindir}/cmd2 ..."
#
#     ALTNERATIVE_PRIORITY - optional, applies to all
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
# with the rest info deduced implicitly
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
if [ "${PN}" = "${BPN}" ] ; then
	for link in ${ALTERNATIVE_LINKS}
	do
		mv ${D}${link} ${D}${link}.${PN}
	done
fi
}

def update_alternatives_after_parse(d):
    if bb.data.getVar('ALTERNATIVE_LINKS', d) != None:
        doinstall = bb.data.getVar('do_install', d, 0)
        doinstall += bb.data.getVar('update_alternatives_batch_doinstall', d, 0)
        bb.data.setVar('do_install', doinstall, d)
        return

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
	if bb.data.getVar('ALTERNATIVE_LINKS', d) != None:
		postinst += bb.data.getVar('update_alternatives_batch_postinst', d, 1)
	else:
		postinst += bb.data.getVar('update_alternatives_postinst', d, 1)
	bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)
	postrm = bb.data.getVar('pkg_postrm_%s' % pkg, d, 1) or bb.data.getVar('pkg_postrm', d, 1)
	if not postrm:
		postrm = '#!/bin/sh\n'
	if bb.data.getVar('ALTERNATIVE_LINKS', d) != None:
		postrm += bb.data.getVar('update_alternatives_batch_postrm', d, 1)
	else:
		postrm += bb.data.getVar('update_alternatives_postrm', d, 1)
	bb.data.setVar('pkg_postrm_%s' % pkg, postrm, d)
}
