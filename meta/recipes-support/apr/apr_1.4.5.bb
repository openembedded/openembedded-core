DESCRIPTION = "Apache Portable Runtime (APR) library"
HOMEPAGE = "http://apr.apache.org/"
SECTION = "libs"

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=0c35ff3c4c83b89d2f076e315caac28b \
                    file://include/apr_lib.h;endline=17;md5=ee42fa7575dc40580a9e01c1b75fae96"

PR = "r1"

SRC_URI = "${APACHE_MIRROR}/apr/${BPN}-${PV}.tar.bz2 \
           file://configure_fixes.patch \
           file://cleanup.patch \
           file://configfix.patch"

SRC_URI[md5sum] = "8b53f5a5669d0597f2da889a2f576eb6"
SRC_URI[sha256sum] = "38c61cacb39be649411cdab212979c71ce29495549c249c2e9a1b0d12480c93e"

inherit autotools lib_package binconfig multilib_header

OE_BINCONFIG_EXTRA_MANGLE = " -e 's:location=source:location=installed:'"

do_configure_prepend() {
	cd ${S}
	./buildconf
}

FILES_${PN}-dev += "${libdir}/apr.exp ${datadir}/build-1/*"

#for some reason, build/libtool.m4 handled by buildconf still be overwritten
#when autoconf, so handle it again.
do_configure_append() {
	cd ${S}
	sed -i -e 's/LIBTOOL=\(.*\)top_build/LIBTOOL=\1apr_build/' build/libtool.m4
	sed -i -e 's/LIBTOOL=\(.*\)top_build/LIBTOOL=\1apr_build/' build/apr_rules.mk
}

do_install_append() {
	oe_multilib_header apr.h
}

SYSROOT_PREPROCESS_FUNCS += "apr_sysroot_preprocess"

apr_sysroot_preprocess () {
	d=${SYSROOT_DESTDIR}${datadir}/apr
	install -d $d/
	cp ${S}/build/apr_rules.mk $d/
	sed -i s,apr_builddir=.*,apr_builddir=,g $d/apr_rules.mk
	sed -i s,apr_builders=.*,apr_builders=,g $d/apr_rules.mk
	sed -i s,LIBTOOL=.*,LIBTOOL=\$\(SHELL\)\ ${HOST_SYS}-libtool,g $d/apr_rules.mk
	sed -i s,\$\(apr_builders\),${STAGING_DATADIR}/apr/,g $d/apr_rules.mk
	cp ${S}/build/mkdir.sh $d/
	cp ${S}/build/make_exports.awk $d/
	cp ${S}/build/make_var_export.awk $d/
}
