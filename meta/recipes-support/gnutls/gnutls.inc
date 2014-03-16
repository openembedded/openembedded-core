SUMMARY = "GNU Transport Layer Security Library"
HOMEPAGE = "http://www.gnu.org/software/gnutls/"
BUGTRACKER = "https://savannah.gnu.org/support/?group=gnutls"
DEPENDS = "zlib lzo libtasn1 libgcrypt (>= 1.4.2) libcap readline"

INC_PR = "r8"

LICENSE = "GPLv3+ & LGPLv2.1+"
LICENSE_${PN} = "LGPLv2.1+"
LICENSE_${PN}-xx = "LGPLv2.1+"
LICENSE_${PN}-bin = "GPLv3+"
LICENSE_${PN}-extra = "GPLv3+"
LICENSE_${PN}-openssl = "GPLv3+"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504 \
                    file://lib/COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                    file://libextra/COPYING;md5=d32239bcb673463ab874e80d47fae504"

SHRT_VER = "${@d.getVar('PV',1).split('.')[0]}.${@d.getVar('PV',1).split('.')[1]}"

SRC_URI = "ftp://ftp.gnutls.org/gcrypt/gnutls/v${SHRT_VER}/gnutls-${PV}.tar.bz2"

inherit autotools-brokensep binconfig pkgconfig gettext lib_package

EXTRA_OECONF="--with-included-opencdk --with-included-libcfg --disable-rpath \
              --with-libtasn1-prefix=${STAGING_DIR_HOST}${prefix} \
              --with-libgcrypt --with-libgcrypt-prefix=${STAGING_DIR_HOST}${prefix} \
              --with-libdl-prefix=${STAGING_DIR_HOST}${prefix} \
              --with-libpthread-prefix=${STAGING_DIR_HOST}${prefix} \
              --with-libreadline-prefix=${STAGING_DIR_HOST}${prefix} \
              --with-libz-prefix=${STAGING_DIR_HOST}${prefix} \
              --with-lzo --disable-guile \
              --without-p11-kit \
              "
do_configure_prepend() {
	for dir in . lib libextra; do
		rm -f ${dir}/aclocal.m4 ${dir}/m4/libtool.m4 ${dir}/m4/lt*.m4
	done
}

PACKAGES =+ "${PN}-openssl ${PN}-extra ${PN}-xx"

FILES_${PN}-dev += "${bindir}/gnutls-cli-debug"
FILES_${PN}-extra = "${libdir}/libgnutls-extra.so.*"
FILES_${PN}-openssl = "${libdir}/libgnutls-openssl.so.*"
FILES_${PN}-xx = "${libdir}/libgnutlsxx.so.*"

LDFLAGS_append_libc-uclibc += " -pthread"

BBCLASSEXTEND = "native"
