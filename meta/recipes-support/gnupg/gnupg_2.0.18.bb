DESCRIPTION = "GNU privacy guard - a free PGP replacement (new v2.x)"
HOMEPAGE = "http://www.gnupg.org/"
LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://COPYING.LIB;md5=6a6a8e020838b23406c81b19c1d46df6"

DEPENDS = "pth libassuan libksba zlib bzip2 readline libgcrypt"
PR = "r1"

inherit autotools gettext

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/${BPN}/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "2f37e0722666a0fedbe4d9f9227ac4d7"
SRC_URI[sha256sum] = "48aedd762ca443fb952a9e859efe3c66706d7c2c9c77c32dbdbac4fe962dae5b"

EXTRA_OECONF = "--disable-ldap \
		--with-zlib=${STAGING_LIBDIR}/.. \
		--with-bzip2=${STAGING_LIBDIR}/.. \
                --with-readline=${STAGING_LIBDIR}/.. \
               "

do_install_append() {
	ln -sf gpg2 ${D}${bindir}/gpg
}
