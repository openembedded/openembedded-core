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

SRC_URI[md5sum] = "48c05f5dfe97cf21ae0ced811aaad750"
SRC_URI[sha256sum] = "00df8902c7cef4d2440d36ca2a45985853eb36c34a4163bc995c3578030eeef5"

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
