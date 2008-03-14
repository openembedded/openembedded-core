LICENSE = "GPL"
SRC_URI = "ftp://ftp.gnupg.org/gcrypt/gpgme/gpgme-${PV}.tar.bz2"
DEPENDS = "libgpg-error pth libassuan"
PR = "r2"

EXTRA_OECONF = "--with-pth=${STAGING_DIR}/${HOST_SYS} --without-pth-test \
                --with-gpg=${bindir}/gpg --without-gpgsm"

inherit autotools binconfig

do_stage() {
	autotools_stage_includes

	install -d ${STAGING_LIBDIR}
	oe_libinstall -C gpgme -so libgpgme ${STAGING_LIBDIR}
	oe_libinstall -C gpgme -so libgpgme-pth ${STAGING_LIBDIR}
	oe_libinstall -C gpgme -so libgpgme-pthread ${STAGING_LIBDIR}

	install -d ${STAGING_DATADIR}/aclocal
	install -m 0644 gpgme/gpgme.m4 ${STAGING_DATADIR}/aclocal/
}

FILES_${PN}-dev += "${bindir}/gpgme-config"
