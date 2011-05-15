DESCRIPTION = "Open Package Manager"
DESCRIPTION_libopkg = "Open Package Manager Library"
DESCRIPTION_update-alternatives-cworth = "Update alternatives"
SECTION = "base"
HOMEPAGE = "http://code.google.com/p/opkg/"
BUGTRACKER = "http://code.google.com/p/opkg/issues/list"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://src/opkg-cl.c;beginline=1;endline=20;md5=321f658c3f6b6c832e25c8850b5dffba"
DEPENDS_virtclass-native = "curl-native"
DEPENDS_virtclass-nativesdk = "curl-nativesdk"

PE = "1"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/opkg"

# Werror gives all kinds bounds issuses with gcc 4.3.3
do_configure_prepend() {
	sed -i -e s:-Werror::g ${S}/libopkg/Makefile.am
}

inherit autotools pkgconfig
EXTRA_OECONF += " --disable-gpg \
                  --disable-openssl \
                  --disable-ssl-curl \
                  --disable-curl \
                  --disable-sha256"

target_localstatedir := "${localstatedir}"
EXTRA_OECONF += "--with-opkglibdir=${localstatedir}/lib"
EXTRA_OECONF_virtclass-native = "--with-opkglibdir=${target_localstatedir}/lib --disable-gpg --disable-curl --disable-openssl"
EXTRA_OECONF_virtclass-nativesdk = "--with-opkglibdir=${target_localstatedir}/lib --disable-gpg --disable-curl --disable-openssl"

#PROVIDES_append_virtclass-native = "virtual/update-alternatives-native"
#RPROVIDES_${PN} += "update-alternatives-native"

BBCLASSEXTEND = "native nativesdk"

PKGSUFFIX = ""
PKGSUFFIX_virtclass-nativesdk = "-nativesdk"

# Define a variable to allow distros to run configure earlier.
# (for example, to enable loading of ethernet kernel modules before networking starts)
POSTINSTALL_INITPOSITION ?= "98"
