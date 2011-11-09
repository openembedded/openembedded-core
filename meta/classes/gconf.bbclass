DEPENDS += "gconf gconf-native"

gconf_postinst() {
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
}

gconf_prerm() {
SCHEMA_LOCATION=/etc/gconf/schemas
for SCHEMA in ${SCHEMA_FILES}; do
	if [ -e $SCHEMA_LOCATION/$SCHEMA ]; then
		HOME=/root GCONF_CONFIG_SOURCE=`gconftool-2 --get-default-source` \
			gconftool-2 \
			--makefile-uninstall-rule $SCHEMA_LOCATION/$SCHEMA > /dev/null
	fi
done
}

python populate_packages_append () {
	import re
	packages = d.getVar('PACKAGES', 1).split()
	pkgdest =  d.getVar('PKGDEST', 1)
	
	for pkg in packages:
		schema_dir = '%s/%s/etc/gconf/schemas' % (pkgdest, pkg)
		schemas = []
		schema_re = re.compile(".*\.schemas$")
		if os.path.exists(schema_dir):
			for f in os.listdir(schema_dir):
				if schema_re.match(f):
					schemas.append(f)
		if schemas != []:
			bb.note("adding gconf postinst and prerm scripts to %s" % pkg)
			bb.data.setVar('SCHEMA_FILES', " ".join(schemas), d)
			postinst = d.getVar('pkg_postinst_%s' % pkg, 1) or d.getVar('pkg_postinst', 1)
			if not postinst:
				postinst = '#!/bin/sh\n'
			postinst += d.getVar('gconf_postinst', 1)
			d.setVar('pkg_postinst_%s' % pkg, postinst)
			prerm = d.getVar('pkg_prerm_%s' % pkg, 1) or d.getVar('pkg_prerm', 1)
			if not prerm:
				prerm = '#!/bin/sh\n'
			prerm += d.getVar('gconf_prerm', 1)
			d.setVar('pkg_prerm_%s' % pkg, prerm)

}
