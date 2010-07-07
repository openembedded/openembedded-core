DESCRIPTION = "Python GTK+ 2.17.x Bindings"
SECTION = "devel/python"
# needs gtk+ 2.17.x
DEPENDS = "gtk+ libglade python-pycairo python-pygobject"
RDEPENDS = "python-shell python-pycairo python-pygobject"
PROVIDES = "python-pygtk2"
SRCNAME = "pygtk"
LICENSE = "LGPL"
PR = "ml8.2"

SRC_URI = "ftp://ftp.gnome.org/pub/gnome/sources/pygtk/2.17/${SRCNAME}-${PV}.tar.bz2 \
           file://fix-gtkunixprint.patch \
           file://prevent_to_get_display_during_import.patch \
           file://nodocs.patch \
           file://acinclude.m4"
S = "${WORKDIR}/${SRCNAME}-${PV}"

EXTRA_OECONF = "--disable-docs --with-python-includes=${STAGING_INCDIR}/../"

inherit autotools pkgconfig distutils-base

do_configure_prepend() {
	install -m 0644 ${WORKDIR}/acinclude.m4 ${S}/
}

# dirty fix #1: remove dependency on python-pygobject-dev
do_install_append() {
	find ${D} -name "*.la"|xargs rm -f
	rm -f ${D}/${bindir}/pygtk-codegen-2.0
	rm -rf ${D}/${libdir}/pkgconfig
}

# dirty fix #2: fix build system paths leaking in
require fix-path.inc

PACKAGES =+ "${PN}-demo"
FILES_${PN}-demo = " ${bindir}/pygtk-demo ${libdir}/pygtk "
RDEPENDS_${PN}-demo = "python-pygtk python-stringold python-lang"

# todo: revamp packaging, package demo seperatly
FILES_${PN}-dev += " ${libdir}/pygtk/2.0 ${bindir}/pygtk-* ${datadir}/pygtk/2.0"

