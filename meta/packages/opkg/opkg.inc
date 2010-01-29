DESCRIPTION = "Open Package Manager"
DESCRIPTION_libopkg = "Open Package Manager Library"
DESCRIPTION_update-alternatives-cworth = "Update alternatives"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "curl gpgme openssl"
DEPENDS_virtclass-native = "curl-native"
DEPENDS_virtclass-nativesdk = "curl-nativesdk"
PV = "0.0+svnr${SRCREV}"
PE = "1"

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;proto=http \
	file://opkg_unarchive.patch;patch=1;maxrev=201 \
	file://add_vercmp.patch;patch=1 \
	file://headerfix.patch;patch=1 \
	file://logfix.patch;patch=1 "

S = "${WORKDIR}/trunk"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/opkg"

# Werror gives all kinds bounds issuses with gcc 4.3.3
do_configure_prepend() {
    sed -i -e s:-Werror::g ${S}/libopkg/Makefile.am
}

inherit autotools_stage pkgconfig

target_libdir := "${libdir}"
EXTRA_OECONF_virtclass-native = "--with-opkglibdir=${target_libdir} --disable-gpg --disable-curl --disable-openssl"
EXTRA_OECONF_virtclass-nativesdk = "--with-opkglibdir=${target_libdir} --disable-gpg --disable-curl --disable-openssl"

#PROVIDES_append_virtclass-native = "virtual/update-alternatives-native"
#RPROVIDES_${PN} += "update-alternatives-native"

BBCLASSEXTEND = "native nativesdk"
