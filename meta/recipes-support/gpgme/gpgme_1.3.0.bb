DESCRIPTION = "GnuPG Made Easy (GPGME) is a library designed to make access to GnuPG easier for applications. It provides a High-Level Crypto API for encryption, decryption, signing, signature verification and key management"
HOMEPAGE = "http://www.gnupg.org/gpgme.html"
BUGTRACKER = "https://bugs.g10code.com/gnupg/index"

LICENSE = "GPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://COPYING.LESSER;md5=bbb461211a33b134d42ed5ee802b37ff \
                    file://src/gpgme.h;endline=23;md5=2775a99d3dd524c4f848ff1c59093038 \
                    file://src/engine.h;endline=22;md5=e96acfaab1cff82dd8fbefddd2f5c436"

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/gpgme/gpgme-${PV}.tar.bz2 \
           file://disable_gpgconf_check.patch;patch=1;pnum=1"

SRC_URI[md5sum] = "4784e3c6086c9c25e9a1b4d9f7c5aa96"
SRC_URI[sha256sum] = "74e040fcd46061f6b0d8b621a2a2a48100a7ba5c9f69e7cf207259a6e2e3e6a1"
DEPENDS = "libgpg-error libassuan pth"
PR = "r0"

EXTRA_OECONF = "--with-pth=${STAGING_DIR_HOST} --without-pth-test \
                --with-gpg=${bindir}/gpg --without-gpgsm"

inherit autotools binconfig

PACKAGES =+ "${PN}-pth ${PN}-pthread"
FILES_${PN}-pth = "${libdir}/libgpgme-pth.so.*"
FILES_${PN}-pthread = "${libdir}/libgpgme-pthread.so.*"
FILES_${PN} = "${libdir}/libgpgme.so.*"
FILES_${PN}-dev += "${bindir}/gpgme-config"
