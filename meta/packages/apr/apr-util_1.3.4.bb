DESCRIPTION = "Apache Portable Runtime (APR) companion library"
SECTION = "libs"
DEPENDS = "apr expat gdbm"
LICENSE = "Apache License, Version 2.0"

PR = "r3"

SRC_URI = "${APACHE_MIRROR}/apr/${P}.tar.gz \
           file://configure_fixes.patch;patch=1"

EXTRA_OECONF = "--with-apr=${STAGING_BINDIR_CROSS}/apr-1-config \ 
		--with-dbm=gdbm \
		--with-gdbm=${STAGING_DIR_HOST}${layout_prefix} \
		--without-sqlite2 \
		--without-sqlite3 \
		--with-expat=${STAGING_DIR_HOST}${layout_prefix}"


inherit autotools_stage lib_package binconfig

OE_BINCONFIG_EXTRA_MANGLE = " -e 's:location=source:location=installed:'"

do_configure_prepend() {
	cp ${STAGING_DATADIR}/apr/apr_rules.mk ${S}/build/rules.mk
}

