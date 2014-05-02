SUMMARY = "Command line tool and library for client-side URL transfers"
HOMEPAGE = "http://curl.haxx.se/"
BUGTRACKER = "http://curl.haxx.se/mail/list.cgi?list=curl-tracker"
SECTION = "console/network"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;beginline=7;md5=3a34942f4ae3fbf1a303160714e664ac"

DEPENDS = "zlib gnutls"
DEPENDS_class-native = "zlib-native openssl-native"
DEPENDS_class-nativesdk = "nativesdk-zlib"

SRC_URI = "http://curl.haxx.se/download/curl-${PV}.tar.bz2 \
           file://pkgconfig_fix.patch \
           file://generate_code_for_disable_manual.patch \
"

# curl likes to set -g0 in CFLAGS, so we stop it
# from mucking around with debug options
#
SRC_URI += " file://configure_ac.patch"

SRC_URI[md5sum] = "e6d1f9d1b59da5062109ffe14e0569a4"
SRC_URI[sha256sum] = "1fbe82b89bcd6b7ccda8cb0ff076edc60e911595030e27689f4abd5ef7f3cfcd"

inherit autotools pkgconfig binconfig multilib_header

EXTRA_OECONF = "--with-zlib=${STAGING_LIBDIR}/../ \
                --without-libssh2 \
                --with-random=/dev/urandom \
                --without-libidn \
                --enable-crypto-auth \
                --disable-ldap \
                --disable-ldaps \
                --with-ca-bundle=${sysconfdir}/ssl/certs/ca-certificates.crt \
                ${CURLGNUTLS} \
                "

CURLGNUTLS = " --with-gnutls=${STAGING_LIBDIR}/../ --without-ssl"
CURLGNUTLS_class-native = "--without-gnutls --with-ssl"
CURLGNUTLS_class-nativesdk = "--without-gnutls --without-ssl"

do_configure_prepend() {
	sed -i s:OPT_GNUTLS/bin:OPT_GNUTLS:g ${S}/configure.ac
}

do_install_append() {
	oe_multilib_header curl/curlbuild.h
}

PACKAGES =+ "libcurl libcurl-dev libcurl-staticdev libcurl-doc"

FILES_lib${BPN} = "${libdir}/lib*.so.*"
RRECOMMENDS_lib${BPN} += "ca-certificates"
FILES_lib${BPN}-dev = "${includedir} \
                      ${libdir}/lib*.so \
                      ${libdir}/lib*.la \
                      ${libdir}/pkgconfig \
                      ${datadir}/aclocal \
                      ${bindir}/*-config"
FILES_lib${BPN}-staticdev = "${libdir}/lib*.a"
FILES_lib${BPN}-doc = "${mandir}/man3 \
                      ${mandir}/man1/curl-config.1"

BBCLASSEXTEND = "native nativesdk"
