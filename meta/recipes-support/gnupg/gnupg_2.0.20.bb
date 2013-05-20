DESCRIPTION = "GNU privacy guard - a free PGP replacement (new v2.x)"
HOMEPAGE = "http://www.gnupg.org/"
LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://COPYING.LIB;md5=6a6a8e020838b23406c81b19c1d46df6"

DEPENDS = "${PTH} libassuan libksba zlib bzip2 readline libgcrypt"
PTH = "pth"
PTH_libc-uclibc = "npth"

inherit autotools gettext

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/${BPN}/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "9d18ee71bb0b10d40d1c8a393bdd7a89"
SRC_URI[sha256sum] = "6e949b7f062cab8a3cf0910f91ecf04cabaad458c0aeeec66298651b8b04b79a"

EXTRA_OECONF = "--disable-ldap \
		--disable-ccid-driver \
                --without-libcurl \
		--with-zlib=${STAGING_LIBDIR}/.. \
		--with-bzip2=${STAGING_LIBDIR}/.. \
                --with-readline=${STAGING_LIBDIR}/.. \
               "

do_install_append() {
	ln -sf gpg2 ${D}${bindir}/gpg
	ln -sf gpgv2 ${D}${bindir}/gpgv
}
