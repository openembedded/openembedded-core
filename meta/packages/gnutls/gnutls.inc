DESCRIPTION = "GNU Transport Layer Security Library"
HOMEPAGE = "http://www.gnu.org/software/gnutls/"
DEPENDS = "zlib libgcrypt lzo"

LICENSE = "LGPL"

SRC_URI = "ftp://ftp.gnutls.org/pub/gnutls/gnutls-${PV}.tar.bz2 \
           file://gnutls-openssl.patch;patch=1 \
           file://gnutls-texinfo-euro.patch;patch=1 \
           file://configure_madness.patch;patch=1 "

inherit autotools binconfig pkgconfig

EXTRA_OECONF="--with-included-opencdk --with-included-libtasn1"

do_stage() {
    oe_libinstall -C lib/.libs -so -a libgnutls ${STAGING_LIBDIR}
    oe_libinstall -C libextra/.libs -so -a libgnutls-extra ${STAGING_LIBDIR}
    oe_libinstall -C libextra/.libs -so -a libgnutls-openssl ${STAGING_LIBDIR}
    autotools_stage_includes

    install -d ${STAGING_DATADIR}/aclocal
    cp ${S}/lib/libgnutls.m4 ${STAGING_DATADIR}/aclocal/
    cp ${S}/libextra/libgnutls-extra.m4 ${STAGING_DATADIR}/aclocal/
}

PACKAGES =+ "${PN}-openssl ${PN}-extra ${PN}-bin ${PN}-xx"

FILES_${PN} = "${libdir}/libgnutls.so.*"
FILES_${PN}-bin = "${bindir}/gnutls-serv \
                   ${bindir}/gnutls-cli \
                   ${bindir}/srptool \
                   ${bindir}/psktool \
                   ${bindir}/certtool \
                   ${bindir}/gnutls-srpcrypt"

FILES_${PN}-dev += "${bindir}/*-config ${bindir}/gnutls-cli-debug"
FILES_${PN}-extra = "${libdir}/libgnutls-extra.so.*"
FILES_${PN}-openssl = "${libdir}/libgnutls-openssl.so.*"
FILES_${PN}-xx = "${libdir}/libgnutlsxx.so.*"
