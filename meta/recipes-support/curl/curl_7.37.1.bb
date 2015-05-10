SUMMARY = "Command line tool and library for client-side URL transfers"
HOMEPAGE = "http://curl.haxx.se/"
BUGTRACKER = "http://curl.haxx.se/mail/list.cgi?list=curl-tracker"
SECTION = "console/network"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;beginline=7;md5=3a34942f4ae3fbf1a303160714e664ac"

SRC_URI = "http://curl.haxx.se/download/curl-${PV}.tar.bz2 \
           file://pkgconfig_fix.patch \
           file://CVE-2014-3613.patch \
           file://CVE-2014-3620.patch \
           file://CVE-2015-3143.patch \
           file://CVE-2015-3144.patch \
           file://CVE-2015-3145.patch \
           file://CVE-2014-3707.patch \
           file://CVE-2014-8150.patch \
           file://CVE-2015-3153.patch \
"

# curl likes to set -g0 in CFLAGS, so we stop it
# from mucking around with debug options
#
SRC_URI += " file://configure_ac.patch"

SRC_URI[md5sum] = "95c627abcf6494f5abe55effe7cd6a57"
SRC_URI[sha256sum] = "c3ef3cd148f3778ddbefb344117d7829db60656efe1031f9e3065fc0faa25136"

inherit autotools pkgconfig binconfig multilib_header

PACKAGECONFIG ??= "${@bb.utils.contains("DISTRO_FEATURES", "ipv6", "ipv6", "", d)} gnutls zlib"
PACKAGECONFIG_class-native = "ipv6 ssl zlib"
PACKAGECONFIG_class-nativesdk = "ipv6 ssl zlib"

PACKAGECONFIG[ipv6] = "--enable-ipv6,--disable-ipv6,"
PACKAGECONFIG[ssl] = "--with-ssl --with-random=/dev/urandom,--without-ssl,openssl"
PACKAGECONFIG[gnutls] = "--with-gnutls,--without-gnutls,gnutls"
PACKAGECONFIG[zlib] = "--with-zlib=${STAGING_LIBDIR}/../,--without-zlib,zlib"
PACKAGECONFIG[rtmpdump] = "--with-librtmp,--without-librtmp,rtmpdump"
PACKAGECONFIG[libssh2] = "--with-libssh2,--without-libssh2,libssh2"

EXTRA_OECONF = "--without-libidn \
                --enable-crypto-auth \
                --disable-ldap \
                --disable-ldaps \
                --with-ca-bundle=${sysconfdir}/ssl/certs/ca-certificates.crt \
"

do_install_append() {
	oe_multilib_header curl/curlbuild.h
}

PACKAGES =+ "lib${BPN} lib${BPN}-dev lib${BPN}-staticdev lib${BPN}-doc"

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
