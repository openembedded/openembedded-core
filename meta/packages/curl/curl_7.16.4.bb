DESCRIPTION = "Command line tool and library for client-side URL transfers."
LICENSE = "MIT"
DEPENDS = "zlib gnutls"
SECTION = "console/network"
PR = "r3"

SRC_URI = "http://curl.haxx.se/download/curl-${PV}.tar.bz2 \
           file://pkgconfig_fix.patch;patch=1"
S = "${WORKDIR}/curl-${PV}"

inherit autotools pkgconfig binconfig

EXTRA_OECONF = "--with-zlib=${STAGING_LIBDIR}/../ \
                --with-gnutls=${STAGING_BINDIR_CROSS}/ \
                --without-ssl \
                --without-libssh2 \
		--with-random=/dev/urandom \
		--without-libidn \
		--enable-crypto-auth \
		"

do_configure_prepend() {
	sed -i s:OPT_GNUTLS/bin:OPT_GNUTLS:g configure.ac
}

do_stage () {
	install -d ${STAGING_INCDIR}/curl
	install -m 0644 ${S}/include/curl/*.h ${STAGING_INCDIR}/curl/
	oe_libinstall -so -a -C lib libcurl ${STAGING_LIBDIR}
}

PACKAGES += "${PN}-certs libcurl libcurl-dev libcurl-doc"

FILES_${PN} = "${bindir}/curl"

FILES_${PN}-certs = "${datadir}/curl/curl-*"
PACKAGE_ARCH_${PN}-certs = "all"

FILES_${PN}-doc = "${mandir}/man1/curl.1"

FILES_lib${PN} = "${libdir}/lib*.so.*"
RRECOMMENDS_lib${PN} += "${PN}-certs"
FILES_lib${PN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.a \
                      ${libdir}/lib*.la \
                      ${libdir}/pkgconfig \
                      ${datadir}/aclocal \
                      ${bindir}/*-config"

FILES_lib${PN}-doc = "${mandir}/man3 \
                      ${mandir}/man1/curl-config.1"

