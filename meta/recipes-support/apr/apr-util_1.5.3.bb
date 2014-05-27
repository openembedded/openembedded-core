SUMMARY = "Apache Portable Runtime (APR) companion library"
HOMEPAGE = "http://apr.apache.org/"
SECTION = "libs"
DEPENDS = "apr expat gdbm"

BBCLASSEXTEND = "native"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=519e0a18e03f7c023070568c14b077bb \
                    file://include/apu_version.h;endline=17;md5=806685a84e71f10c80144c48eb35df42"

SRC_URI = "${APACHE_MIRROR}/apr/${BPN}-${PV}.tar.gz \
           file://configfix.patch \
           file://configure_fixes.patch \
           file://run-ptest \
"

SRC_URI[md5sum] = "71a11d037240b292f824ba1eb537b4e3"
SRC_URI[sha256sum] = "76db34cb508e346e3bf69347c29ed1500bf0b71bcc48d54271ad9d1c25703743"

EXTRA_OECONF = "--with-apr=${STAGING_BINDIR_CROSS}/apr-1-config \ 
		--without-odbc \
		--without-pgsql \
		--with-dbm=gdbm \
		--with-gdbm=${STAGING_DIR_HOST}${prefix} \
		--without-sqlite2 \
		--without-sqlite3 \
		--with-expat=${STAGING_DIR_HOST}${prefix}"


inherit autotools-brokensep lib_package binconfig

OE_BINCONFIG_EXTRA_MANGLE = " -e 's:location=source:location=installed:'"

do_configure_append() {
	if [ "${CLASSOVERRIDE}" = "class-target" ]; then
		cp ${STAGING_DATADIR}/apr/apr_rules.mk ${S}/build/rules.mk
	fi
}
do_configure_prepend_class-native() {
	cp ${STAGING_DATADIR_NATIVE}/apr/apr_rules.mk ${S}/build/rules.mk
}
do_configure_append_class-native() {
	sed -i "s#LIBTOOL=\$(SHELL) \$(apr_builddir)#LIBTOOL=\$(SHELL) ${STAGING_BINDIR_NATIVE}#" ${S}/build/rules.mk
	# sometimes there isn't SHELL
	sed -i "s#LIBTOOL=\$(apr_builddir)#LIBTOOL=${STAGING_BINDIR_NATIVE}#" ${S}/build/rules.mk
}

FILES_${PN}     += "${libdir}/apr-util-1/apr_dbm_gdbm-1.so"
FILES_${PN}-dev += "${libdir}/aprutil.exp ${libdir}/apr-util-1/apr_dbm_gdbm.so* ${libdir}/apr-util-1/apr_dbm_gdbm.la"
FILES_${PN}-dbg += "${libdir}/apr-util-1/.debug/*"
FILES_${PN}-staticdev += "${libdir}/apr-util-1/apr_dbm_gdbm.a"

inherit ptest

do_compile_ptest() {
	cd ${S}/test
	oe_runmake
}

do_install_ptest() {
	t=${D}${PTEST_PATH}/test
	mkdir $t
	for i in testall data; do \
	  cp -r ${S}/test/$i $t; \
	done
}
