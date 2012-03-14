DESCRIPTION = "GnuPG Made Easy (GPGME) is a library designed to make access to GnuPG easier for applications. It provides a High-Level Crypto API for encryption, decryption, signing, signature verification and key management"
HOMEPAGE = "http://www.gnupg.org/gpgme.html"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LESSER;md5=bbb461211a33b134d42ed5ee802b37ff \
                    file://src/gpgme.h.in;endline=23;md5=942b47052c0674c6a3b9b9e6127628a4 \
                    file://src/engine.h;endline=22;md5=e96acfaab1cff82dd8fbefddd2f5c436"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/gpgme/gpgme-${PV}.tar.bz2 \
           file://disable_gpgconf_check.patch"

SRC_URI[md5sum] = "90afa8436ce2b2683c001c824bd22601"
SRC_URI[sha256sum] = "15ef27a875ae0d79d7446fd931deda11438e724ffbeac74449ed19cba23df4d4"
DEPENDS = "libgpg-error libassuan pth"
PR = "r1"

EXTRA_OECONF = "--with-pth=${STAGING_DIR_HOST} --without-pth-test \
                --with-gpg=${bindir}/gpg --without-gpgsm"

inherit autotools binconfig

PACKAGES =+ "${PN}-pth ${PN}-pthread"
FILES_${PN}-pth = "${libdir}/libgpgme-pth.so.*"
FILES_${PN}-pthread = "${libdir}/libgpgme-pthread.so.*"
FILES_${PN} = "${libdir}/libgpgme.so.*"
FILES_${PN}-dev += "${bindir}/gpgme-config ${datadir}/common-lisp/source/gpgme/*"

do_install_append() {
	rmdir ${D}/usr/libexec
}
