DESCRIPTION = "Apache Portable Runtime (APR) companion library"
SECTION = "libs"
DEPENDS = "apr expat gdbm"
LICENSE = "Apache License, Version 2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=519e0a18e03f7c023070568c14b077bb"
PR = "r7"

SRC_URI = "${APACHE_MIRROR}/apr/${P}.tar.gz \
           file://configfix.patch;patch=1 \
           file://configure_fixes.patch;patch=1"

SRC_URI[md5sum] = "a10e2ca150ff07f484c724c36142211f"
SRC_URI[sha256sum] = "3f07ffdb18fb853290c9b83e82cd5cae66b8fbc357bd391e846c0afdd24fed7e"

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

