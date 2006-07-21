SECTION = "console/utils"
DESCRIPTION = "pkg-config is a system for managing library \
compile/link flags that works with automake and autoconf. \
It replaces the ubiquitous *-config scripts you may have \
seen with a single tool."
HOMEPAGE = "http://www.freedesktop.org/software/pkgconfig/"
LICENSE = "GPL"
PR = "r2"

SRC_URI = "http://www.freedesktop.org/software/pkgconfig/releases/pkgconfig-${PV}.tar.gz \
           file://configure.patch;patch=1 \
           file://pkg.m4 \
           file://glibconfig-sysdefs.h"

inherit autotools 

acpaths = "-I ."
do_configure_prepend () {
	install -m 0644 ${WORKDIR}/glibconfig-sysdefs.h glib-1.2.8/
	install -m 0644 ${WORKDIR}/pkg.m4 ${S}/
}

do_stage_append() {
	install -d -m 0755 ${STAGING_DATADIR}/pkgconfig

}
