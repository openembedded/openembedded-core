DESCRIPTION = "Apache Portable Runtime (APR) companion library"
HOMEPAGE = "http://apr.apache.org/"
SECTION = "libs"
DEPENDS = "apr expat gdbm"

BBCLASSEXTEND = "native"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=519e0a18e03f7c023070568c14b077bb \
                    file://include/apu_version.h;endline=17;md5=806685a84e71f10c80144c48eb35df42"

PR = "r1"

SRC_URI = "${APACHE_MIRROR}/apr/${BPN}-${PV}.tar.gz \
           file://configfix.patch \
           file://configure_fixes.patch"

SRC_URI[md5sum] = "666a5d56098a9debf998510e304c8095"
SRC_URI[sha256sum] = "d636d9ef95c6e50e47fc338d532aa375edd11e5d7a3c30dee48beb38ddf4ab4c"

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
do_configure_prepend_virtclass-native() {
	cp ${STAGING_DATADIR_NATIVE}/apr/apr_rules.mk ${S}/build/rules.mk
}
do_configure_append_virtclass-native() {
	sed -i "s#LIBTOOL=\$(SHELL) \$(apr_builddir)#LIBTOOL=\$(SHELL) ${STAGING_BINDIR_NATIVE}#" ${S}/build/rules.mk
	# sometimes there isn't SHELL
	sed -i "s#LIBTOOL=\$(apr_builddir)#LIBTOOL=${STAGING_BINDIR_NATIVE}#" ${S}/build/rules.mk
}

FILES_${PN}     += "${libdir}/apr-util-1/apr_dbm_gdbm-1.so"
FILES_${PN}-dev += "${libdir}/aprutil.exp ${libdir}/apr-util-1/apr_dbm_gdbm.so* ${libdir}/apr-util-1/apr_dbm_gdbm.la"
FILES_${PN}-dbg += "${libdir}/apr-util-1/.debug/*"
FILES_${PN}-staticdev += "${libdir}/apr-util-1/apr_dbm_gdbm.a"
