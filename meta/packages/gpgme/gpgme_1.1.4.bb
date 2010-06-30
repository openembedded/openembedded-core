DESCRIPTION = "GnuPG Made Easy (GPGME) is a library designed to make access to GnuPG easier for applications. It provides a High-Level Crypto API for encryption, decryption, signing, signature verification and key management"
HOMEPAGE = "http://www.gnupg.org/gpgme.html"
LICENSE = "GPLv2+ & LGPLv2.1+"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/gpgme/gpgme-${PV}.tar.bz2"
DEPENDS = "libgpg-error pth"
PR = "r4"

EXTRA_OECONF = "--with-pth=${STAGING_DIR_HOST} --without-pth-test \
                --with-gpg=${bindir}/gpg --without-gpgsm"

inherit autotools binconfig

PACKAGES =+ "${PN}-pth ${PN}-pthread"
FILES_${PN}-pth = "${libdir}/libgpgme-pth.so.*"
FILES_${PN}-pthread = "${libdir}/libgpgme-pthread.so.*"
FILES_${PN} = "${libdir}/libgpgme.so.*"
FILES_${PN}-dev += "${bindir}/gpgme-config"
