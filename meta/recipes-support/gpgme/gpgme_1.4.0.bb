DESCRIPTION = "GnuPG Made Easy (GPGME) is a library designed to make access to GnuPG easier for applications. It provides a High-Level Crypto API for encryption, decryption, signing, signature verification and key management"
HOMEPAGE = "http://www.gnupg.org/gpgme.html"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LESSER;md5=bbb461211a33b134d42ed5ee802b37ff \
                    file://src/gpgme.h.in;endline=23;md5=a7b9706396e8e12c17be7d8d556ef7a5 \
                    file://src/engine.h;endline=22;md5=4b6d8ba313d9b564cc4d4cfb1640af9d"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/gpgme/gpgme-${PV}.tar.bz2 \
           file://disable_gpgconf_check.patch"

SRC_URI[md5sum] = "a0f93aba6d8a831ba14905085027f2f9"
SRC_URI[sha256sum] = "728c959abb0795ed4357b308cdbba92210d22998b075664d1e038d3cc7145619"
DEPENDS = "libgpg-error libassuan ${PTH}"
PTH_libc-uclibc = "npth"
PTH = "pth"

PR = "r0"

EXTRA_OECONF = "--with-pth=${STAGING_DIR_HOST} --without-pth-test \
                --with-gpg=${bindir}/gpg --without-gpgsm"

inherit autotools binconfig

PACKAGES =+ "${PN}-pth ${PN}-pthread"
FILES_${PN}-pth = "${libdir}/libgpgme-pth.so.*"
FILES_${PN}-pthread = "${libdir}/libgpgme-pthread.so.*"
FILES_${PN}-dev += "${datadir}/common-lisp/source/gpgme/*"
