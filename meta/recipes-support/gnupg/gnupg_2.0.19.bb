DESCRIPTION = "GNU privacy guard - a free PGP replacement (new v2.x)"
HOMEPAGE = "http://www.gnupg.org/"
LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://COPYING.LIB;md5=6a6a8e020838b23406c81b19c1d46df6"

DEPENDS = "${PTH} libassuan libksba zlib bzip2 readline libgcrypt"
PTH = "pth"
PTH_libc-uclibc = "npth"
PR = "r5"

inherit autotools gettext

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://GnuPG2-CVE-2012-6085.patch"

SRC_URI[md5sum] = "6a8589381ca1b0c1a921e9955f42b016"
SRC_URI[sha256sum] = "efa23a8a925adb51c7d3b708c25b6d000300f5ce37de9bdec6453be7b419c622"

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
