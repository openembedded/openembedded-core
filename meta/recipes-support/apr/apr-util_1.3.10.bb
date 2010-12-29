DESCRIPTION = "Apache Portable Runtime (APR) companion library"
HOMEPAGE = "http://apr.apache.org/"
SECTION = "libs"
DEPENDS = "apr expat gdbm"

LICENSE = "Apache License, Version 2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=519e0a18e03f7c023070568c14b077bb \
                    file://include/apu_version.h;endline=17;md5=806685a84e71f10c80144c48eb35df42"

PR = "r0"

SRC_URI = "${APACHE_MIRROR}/apr/${P}.tar.gz \
           file://configfix.patch;patch=1 \
           file://configure_fixes.patch;patch=1"

SRC_URI[md5sum] = "82acd25cf3df8c72eba44eaee8b80c19"
SRC_URI[sha256sum] = "7c37ac40b2351bfc23000fb6b7b54a67e0872255df315c82eb60c821bcef4b23"

EXTRA_OECONF = "--with-apr=${STAGING_BINDIR_CROSS}/apr-1-config \ 
		--without-odbc \
		--with-dbm=gdbm \
		--with-gdbm=${STAGING_DIR_HOST}${prefix} \
		--without-sqlite2 \
		--without-sqlite3 \
		--with-expat=${STAGING_DIR_HOST}${prefix}"


inherit autotools lib_package binconfig

OE_BINCONFIG_EXTRA_MANGLE = " -e 's:location=source:location=installed:'"

do_configure_prepend() {
	cp ${STAGING_DATADIR}/apr/apr_rules.mk ${S}/build/rules.mk
}

