DESCRIPTION = "Command line tool and library for client-side URL transfers."
LICENSE = "MIT"
DEPENDS = "zlib"
SECTION = "console/network"
RPROVIDES_lib${PN} += "libcurl"

SRC_URI = "http://curl.haxx.se/download/curl-${PV}.tar.bz2"
S = "${WORKDIR}/curl-${PV}"

inherit autotools pkgconfig binconfig

EXTRA_OECONF = "--with-zlib=${STAGING_LIBDIR}/../ \
		--without-ssl \
		--with-random=/dev/urandom \
		--without-idn \
		--enable-http \
		--enable-file"

do_stage () {
	install -d ${STAGING_INCDIR}/curl
	install -m 0644 ${S}/include/curl/*.h ${STAGING_INCDIR}/curl/
	oe_libinstall -so -a -C lib libcurl ${STAGING_LIBDIR}
}

PACKAGES = "curl curl-doc libcurl libcurl-dev libcurl-doc"
FILES_${PN} = "${bindir}/curl"
FILES_${PN}-doc = "${mandir}/man1/curl.1"
FILES_lib${PN} = "${libdir}/lib*.so.*"
FILES_lib${PN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.a \
                      ${libdir}/lib*.la \
                      ${libdir}/pkgconfig \
                      ${datadir}/aclocal \
                      ${bindir}/*-config"
FILES_lib${PN}-doc = "${mandir}/man3 \
                      ${mandir}/man1/curl-config.1"

