SUMMARY = "Helper tool used when compiling"
DESCRIPTION = "pkg-config is a helper tool used when compiling applications and libraries. It helps determined \
the correct compiler/link options.  It is also language-agnostic."
HOMEPAGE = "http://pkg-config.freedesktop.org/wiki/"
BUGTRACKER = "http://bugs.freedesktop.org/buglist.cgi?product=pkg-config"
SECTION = "console/utils"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "glib-2.0 popt"
DEPENDS_virtclass-native = ""
DEPENDS_virtclass-nativesdk = ""

SRC_URI = "http://pkgconfig.freedesktop.org/releases/pkg-config-${PV}.tar.gz \
           file://autofoo.patch \
           file://glibconfig-sysdefs.h \
           file://disable-legacy.patch"

S = "${WORKDIR}/pkg-config-${PV}/"

inherit autotools

EXTRA_OECONF = "--with-installed-glib --with-installed-popt --disable-legacy-scripts"
EXTRA_OECONF_virtclass-native = "--disable-legacy-scripts"
EXTRA_OECONF_virtclass-nativesdk = "--disable-legacy-scripts"

acpaths = "-I ."
do_configure_prepend () {
	install -m 0644 ${WORKDIR}/glibconfig-sysdefs.h glib-1.2.10/
}

BBCLASSEXTEND = "native nativesdk"

FILES_${PN} += "${datadir}/aclocal/pkg.m4"
