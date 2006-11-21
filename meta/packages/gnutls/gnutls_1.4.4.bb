DESCRIPTION = "GNU Transport Layer Security Library"
DEPENDS = "zlib libgcrypt lzo"
HOMEPAGE = "http://www.gnu.org/software/gnutls/"
LICENSE = "LGPL"

SRC_URI = "ftp://ftp.gnutls.org/pub/gnutls/gnutls-${PV}.tar.bz2 \
	   file://onceonly.m4 \
           file://gnutls-openssl.patch;patch=1 \
           file://gnutls-texinfo-euro.patch;patch=1"

inherit autotools binconfig

do_configure_prepend() {
        cp ${WORKDIR}/onceonly.m4 ${S}/m4/
}

PACKAGES =+ "${PN}-openssl ${PN}-extra ${PN}-bin"
FILES_${PN}-openssl = "${libdir}/libgnutls-openssl.so.*"
FILES_${PN}-extra = "${libdir}/libgnutls-extra.so.*"
FILES_${PN} = "${libdir}/libgnutls.so.*"
FILES_${PN}-bin = "${bindir}/gnutls-serv \
		   ${bindir}/gnutls-cli \
		   ${bindir}/srptool \
		   ${bindir}/certtool \
		   ${bindir}/gnutls-srpcrypt \
		   ${bindir}/psktool"

FILES_${PN}-dev += "${bindir}/*-config ${bindir}/gnutls-cli-debug"

EXTRA_OECONF="--with-included-opencdk --with-included-libtasn1"

do_stage() {
	oe_libinstall -C lib/.libs -so -a libgnutls ${STAGING_LIBDIR}
	oe_libinstall -C libextra/.libs -so -a libgnutls-extra ${STAGING_LIBDIR}
	oe_libinstall -C libextra/.libs -so -a libgnutls-openssl ${STAGING_LIBDIR}
	autotools_stage_includes

	install -d ${STAGING_DATADIR}/aclocal
	cp ${S}/lib/libgnutls.m4 ${STAGING_DATADIR}/aclocal/
}

