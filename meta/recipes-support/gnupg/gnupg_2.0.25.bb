SUMMARY = "GNU Privacy Guard - encryption and signing tools (2.x)"
HOMEPAGE = "http://www.gnupg.org/"
LICENSE = "GPLv3 & LGPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=f27defe1e96c2e1ecd4e0c9be8967949 \
                    file://COPYING.LIB;md5=6a6a8e020838b23406c81b19c1d46df6"

DEPENDS = "${PTH} libassuan libksba zlib bzip2 readline libgcrypt"
PTH = "pth"
PTH_libc-uclibc = "npth"

inherit autotools gettext texinfo pkgconfig

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/${BPN}/${BPN}-${PV}.tar.bz2 \
           file://pkgconfig.patch"

SRC_URI[md5sum] = "048d8de797125f1d8e6cf3bdff9e2d12"
SRC_URI[sha256sum] = "b3abe97df096cf53d6a422ac922f3c48e688bc892b7ccf669b92f0b7fc6c5823"

EXTRA_OECONF = "--disable-ldap \
		--disable-ccid-driver \
                --without-libcurl \
		--with-zlib=${STAGING_LIBDIR}/.. \
		--with-bzip2=${STAGING_LIBDIR}/.. \
                --with-readline=${STAGING_LIBDIR}/.. \
               "

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
