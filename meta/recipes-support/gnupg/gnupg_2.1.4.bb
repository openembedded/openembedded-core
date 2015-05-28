SUMMARY = "GNU Privacy Guard - encryption and signing tools (2.x)"
HOMEPAGE = "http://www.gnupg.org/"
LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://COPYING.LIB;md5=6a6a8e020838b23406c81b19c1d46df6"

DEPENDS = "npth libassuan libksba zlib bzip2 readline libgcrypt"

inherit autotools gettext texinfo pkgconfig

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://pkgconfig.patch \
           file://use-pkgconfig-instead-of-npth-config.patch \
           file://dirmngr-uses-libgpg-error.patch \
          "

SRC_URI[md5sum] = "c36f1a4c261ecc81fa5df551356c33f1"
SRC_URI[sha256sum] = "64127eedd868510f2bccccb22c507a4878ffa07495db16a0f976c67f56426cb0"

EXTRA_OECONF = "--disable-ldap \
		--disable-ccid-driver \
		--with-zlib=${STAGING_LIBDIR}/.. \
		--with-bzip2=${STAGING_LIBDIR}/.. \
                --with-readline=${STAGING_LIBDIR}/.. \
               "
RRECOMMENDS_${PN} = "pinentry"

do_configure_prepend () {
	# Else these could be used in prefernce to those in aclocal-copy
	rm -f ${S}/m4/gpg-error.m4
	rm -f ${S}/m4/libassuan.m4
	rm -f ${S}/m4/ksba.m4
	rm -f ${S}/m4/libgcrypt.m4
}

do_install_append() {
	ln -sf gpg2 ${D}${bindir}/gpg
	ln -sf gpgv2 ${D}${bindir}/gpgv
}

RDEPENDS_${PN} = "gnutls"
