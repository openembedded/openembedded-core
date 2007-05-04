DESCRIPTION = "GNOME XSLT library"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "libxml2"
LICENSE = "MIT"
PR = "r2"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxslt-${PV}.tar.gz \
           file://destdir.patch;patch=1"
S = "${WORKDIR}/libxslt-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-python --without-debug --without-mem-debug --without-crypto"

xsltheaders = "attributes.h documents.h extensions.h extra.h functions.h imports.h \
               keys.h namespaces.h numbersInternals.h pattern.h preproc.h security.h \
               templates.h transform.h variables.h xslt.h xsltInternals.h xsltconfig.h \
               xsltexports.h xsltutils.h"
exsltheaders = "exslt.h exsltconfig.h exsltexports.h"

do_stage () {
	oe_libinstall -C libxslt -so -a libxslt ${STAGING_LIBDIR}
	oe_libinstall -C libexslt -so -a libexslt ${STAGING_LIBDIR}

	mkdir -p ${STAGING_INCDIR}/libxslt
	for i in ${xsltheaders}; do
		install -m 0644 ${S}/libxslt/$i ${STAGING_INCDIR}/libxslt/$i
	done
	mkdir -p ${STAGING_INCDIR}/libexslt
	for i in ${exsltheaders}; do
		install -m 0644 ${S}/libexslt/$i ${STAGING_INCDIR}/libexslt/$i
	done

	cat xslt-config | sed -e "s,^prefix=.*,prefix=${STAGING_DIR}/${HOST_SYS}," \
		       	     -e "s,^exec_prefix=.*,exec_prefix=${STAGING_DIR}/${HOST_SYS}," \
			     -e "s,^includedir=.*,includedir=${STAGING_INCDIR}," \
			     -e "s,^libdir=.*,libdir=${STAGING_LIBDIR}," > ${STAGING_BINDIR_CROSS}/xslt-config
	chmod a+rx ${STAGING_BINDIR_CROSS}/xslt-config
	install -m 0644 libxslt.m4 ${STAGING_DATADIR}/aclocal/
}

PACKAGES = "${PN}-dbg ${PN}-dev ${PN}-utils ${PN} ${PN}-doc ${PN}-locale"

FILES_${PN}-dev += "${bindir}/xslt-config"
FILES_${PN}-utils += "${bindir}"
