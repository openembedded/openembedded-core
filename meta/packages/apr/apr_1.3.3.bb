DESCRIPTION = "Apache Portable Runtime (APR) library"
SECTION = "libs"
LICENSE = "Apache License, Version 2.0"

PR = "r4"

SRC_URI = "${APACHE_MIRROR}/apr/${P}.tar.bz2 \
           file://configure_fixes.patch;patch=1 \
	   file://cleanup.patch;patch=1 \
           file://configfix.patch;patch=1"

inherit autotools lib_package binconfig

OE_BINCONFIG_EXTRA_MANGLE = " -e 's:location=source:location=installed:'"

do_configure_prepend() {
	cd ${S}
	./buildconf
}

do_stage() {
	autotools_stage_all
	install -d ${STAGING_DATADIR}/apr
	cp ${S}/build/apr_rules.mk ${STAGING_DATADIR}/apr/
	sed -i s,apr_builddir=.*,apr_builddir=,g ${STAGING_DATADIR}/apr/apr_rules.mk
	sed -i s,apr_builders=.*,apr_builders=,g ${STAGING_DATADIR}/apr/apr_rules.mk
	sed -i s,LIBTOOL=.*,LIBTOOL=\$\(SHELL\)\ ${TARGET_PREFIX}libtool,g ${STAGING_DATADIR}/apr/apr_rules.mk
	sed -i s,\$\(apr_builders\),${STAGING_DATADIR}/apr/,g ${STAGING_DATADIR}/apr/apr_rules.mk
	cp ${S}/build/mkdir.sh ${STAGING_DATADIR}/apr/
	cp ${S}/build/make_exports.awk ${STAGING_DATADIR}/apr/
	cp ${S}/build/make_var_export.awk ${STAGING_DATADIR}/apr/
}
