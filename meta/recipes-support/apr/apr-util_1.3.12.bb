DESCRIPTION = "Apache Portable Runtime (APR) companion library"
HOMEPAGE = "http://apr.apache.org/"
SECTION = "libs"
DEPENDS = "apr expat gdbm"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=519e0a18e03f7c023070568c14b077bb \
                    file://include/apu_version.h;endline=17;md5=806685a84e71f10c80144c48eb35df42"

PR = "r1"

SRC_URI = "${APACHE_MIRROR}/apr/${BPN}-${PV}.tar.gz \
           file://configfix.patch \
           file://configure_fixes.patch"

SRC_URI[md5sum] = "d1977289889592ef998e3f777f68efe4"
SRC_URI[sha256sum] = "815b6fc82950f61050a5e711a7f3c20fd9b6ffcc7a4cacfe9f291fb241210cd8"

EXTRA_OECONF = "--with-apr=${STAGING_BINDIR_CROSS}/apr-1-config \ 
		--without-odbc \
		--without-pgsql \
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

FILES_${PN}     += "${libdir}/apr-util-1/apr_dbm_gdbm-1.so"
FILES_${PN}-dev += "${libdir}/aprutil.exp ${libdir}/apr-util-1/apr_dbm_gdbm.*"
FILES_${PN}-dbg += "${libdir}/apr-util-1/.debug/*"

