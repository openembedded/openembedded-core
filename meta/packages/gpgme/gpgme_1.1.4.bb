DESCRIPTION = "GPGME - GnuPG Made Easy"
LICENSE = "GPL"
SRC_URI = "ftp://ftp.gnupg.org/gcrypt/gpgme/gpgme-${PV}.tar.bz2"
DEPENDS = "libgpg-error pth"
PR = "r3"

EXTRA_OECONF = "--with-pth=${STAGING_DIR}/${HOST_SYS} --without-pth-test \
                --with-gpg=${bindir}/gpg --without-gpgsm"

inherit autotools_stage binconfig

PACKAGES =+ "${PN}-pth ${PN}-pthread"
FILES_${PN}-pth = "${libdir}/libgpgme-pth.so.*"
FILES_${PN}-pthread = "${libdir}/libgpgme-pthread.so.*"
FILES_${PN} = "${libdir}/libgpgme.so.*"
FILES_${PN}-dev += "${bindir}/gpgme-config"
