gconf_postinst() {
if [ "$1" = configure ]; then
	if [ "x$D" != "x" ]; then
		exit 1
	fi
        SCHEMA_LOCATION=/etc/gconf/schemas
        for SCHEMA in ${SCHEMA_FILES}; do
                if [ -e $SCHEMA_LOCATION/$SCHEMA ]; then
                        HOME=/root GCONF_CONFIG_SOURCE=`gconftool-2 --get-default-source` \
                                gconftool-2 \
                                --makefile-install-rule $SCHEMA_LOCATION/$SCHEMA > /dev/null
                fi
        done
fi
}

gconf_prerm() {
if [ "$1" = remove ] || [ "$1" = upgrade ]; then
        SCHEMA_LOCATION=/etc/gconf/schemas
        for SCHEMA in ${SCHEMA_FILES}; do
                if [ -e $SCHEMA_LOCATION/$SCHEMA ]; then
                        HOME=/root GCONF_CONFIG_SOURCE=`gconftool-2 --get-default-source` \
                                gconftool-2 \
                                        --makefile-uninstall-rule $SCHEMA_LOCATION/$SCHEMA > /dev/null
                fi
        done
fi
}

python populate_packages_append () {
	import os.path, re
	packages = bb.data.getVar('PACKAGES', d, 1).split()
	workdir = bb.data.getVar('WORKDIR', d, 1)
	
	for pkg in packages:
		schema_dir = '%s/install/%s/etc/gconf/schemas' % (workdir, pkg)
		schemas = []
		schema_re = re.compile(".*\.schemas$")
		if os.path.exists(schema_dir):
			for f in os.listdir(schema_dir):
				if schema_re.match(f):
					schemas.append(f)
		if schemas != []:
			bb.note("adding gconf postinst and prerm scripts to %s" % pkg)
			bb.data.setVar('SCHEMA_FILES', " ".join(schemas), d)
			postinst = bb.data.getVar('pkg_postinst_%s' % pkg, d, 1) or bb.data.getVar('pkg_postinst', d, 1)
			if not postinst:
				postinst = '#!/bin/sh\n'
			postinst += bb.data.getVar('gconf_postinst', d, 1)
			bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)
			prerm = bb.data.getVar('pkg_prerm_%s' % pkg, d, 1) or bb.data.getVar('pkg_prerm', d, 1)
			if not prerm:
				prerm = '#!/bin/sh\n'
			prerm += bb.data.getVar('gconf_prerm', d, 1)
			bb.data.setVar('pkg_prerm_%s' % pkg, prerm, d)

}
