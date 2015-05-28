SUMMARY = "High-level GnuPG encryption/signing API"
DESCRIPTION = "GnuPG Made Easy (GPGME) is a library designed to make access to GnuPG easier for applications. It provides a High-Level Crypto API for encryption, decryption, signing, signature verification and key management"
HOMEPAGE = "http://www.gnupg.org/gpgme.html"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LESSER;md5=bbb461211a33b134d42ed5ee802b37ff \
                    file://src/gpgme.h.in;endline=23;md5=dccb4bb624011e36513c61ef0422d054 \
                    file://src/engine.h;endline=22;md5=4b6d8ba313d9b564cc4d4cfb1640af9d"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/gpgme/gpgme-${PV}.tar.bz2 \
           file://disable_gpgconf_check.patch \
           file://gpgme.pc \
           file://gpgme-fix-CVE-2014-3564.patch \
          "

SRC_URI[md5sum] = "334e524cffa8af4e2f43ae8afe585672"
SRC_URI[sha256sum] = "2d1cc12411753752d9c5b9037e6fd3fd363517af720154768cc7b46b60120496"

DEPENDS = "libgpg-error libassuan"

EXTRA_OECONF = "--with-gpg=${bindir}/gpg --without-gpgsm"

BINCONFIG = "${bindir}/gpgme-config"

inherit autotools texinfo binconfig-disabled pkgconfig

PACKAGES =+ "${PN}-pthread"
FILES_${PN}-pthread = "${libdir}/libgpgme-pthread.so.*"
FILES_${PN}-dev += "${datadir}/common-lisp/source/gpgme/*"

do_configure_prepend () {
	# Else these could be used in preference to those in aclocal-copy
	rm -f ${S}/m4/gpg-error.m4
	rm -f ${S}/m4/libassuan.m4
}

do_install_append () {
        install -d ${D}${libdir}/pkgconfig
        install -m 0644 ${WORKDIR}/gpgme.pc ${D}${libdir}/pkgconfig/
}
