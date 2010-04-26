DESCRIPTION = "GNU Transport Layer Security Library"
HOMEPAGE = "http://www.gnu.org/software/gnutls/"
DEPENDS = "zlib libgcrypt lzo gettext"

LICENSE = "LGPL"

SRC_URI = "ftp://ftp.gnutls.org/pub/gnutls/gnutls-${PV}.tar.bz2"

inherit autotools binconfig pkgconfig pkgconfig

EXTRA_OECONF="--with-included-opencdk --with-included-libtasn1 --with-included-libcfg --with-libz-prefix=${STAGING_DIR_HOST}${prefix} --with-lzo --disable-guile"

PACKAGES =+ "${PN}-openssl ${PN}-extra ${PN}-bin ${PN}-xx"

FILES_${PN} = "${libdir}/libgnutls.so.*"
FILES_${PN}-bin = "${bindir}/gnutls-serv \
                   ${bindir}/gnutls-cli \
                   ${bindir}/srptool \
                   ${bindir}/psktool \
                   ${bindir}/certtool \
                   ${bindir}/gnutls-srpcrypt"

FILES_${PN}-dev += "${bindir}/*-config ${bindir}/gnutls-cli-debug"
FILES_${PN}-extra = "${libdir}/libgnutls-extra.so.*"
FILES_${PN}-openssl = "${libdir}/libgnutls-openssl.so.*"
FILES_${PN}-xx = "${libdir}/libgnutlsxx.so.*"
