SECTION = "console/utils"
DESCRIPTION = "pkg-config is a system for managing library \
compile/link flags that works with automake and autoconf. \
It replaces the ubiquitous *-config scripts you may have \
seen with a single tool."
HOMEPAGE = "http://pkg-config.freedesktop.org/wiki/"
LICENSE = "GPL"
PR = "r7"

DEPENDS = "glib-2.0"
DEPENDS_virtclass-native = ""
DEPENDS_virtclass-nativesdk = ""
EXTRA_OECONF = "--with-installed-glib"
EXTRA_OECONF_virtclass-native = ""
EXTRA_OECONF_virtclass-nativesdk = ""

SRC_URI = "http://pkgconfig.freedesktop.org/releases/pkg-config-${PV}.tar.gz \
           file://autofoo.patch;patch=1 \
           file://sysrootfix.patch;patch=1 \
           file://glibconfig-sysdefs.h"

S = "${WORKDIR}/pkg-config-${PV}/"

inherit autotools

acpaths = "-I ."
do_configure_prepend () {
	install -m 0644 ${WORKDIR}/glibconfig-sysdefs.h glib-1.2.10/
}

BBCLASSEXTEND = "native nativesdk"
