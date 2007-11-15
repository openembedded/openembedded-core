DESCRIPTION = "Python GTK+ 2.10.x Bindings"
SECTION = "devel/python"
# needs gtk+ 2.10.x
DEPENDS = "gtk+ libglade python-pycairo python-pygobject"
RDEPENDS = "python-shell python-pycairo python-pygobject"
PROVIDES = "python-pygtk2"
SRCNAME = "pygtk"
LICENSE = "LGPL"
PR = "ml3"

SRC_URI = "ftp://ftp.gnome.org/pub/gnome/sources/pygtk/2.10/${SRCNAME}-${PV}.tar.bz2 \
           file://fix-gtkunixprint.patch;patch=1 \
           file://acinclude.m4"
S = "${WORKDIR}/${SRCNAME}-${PV}"

EXTRA_OECONF = "--disable-docs"
EXTRA_OECONF += "--with-python-includes=${STAGING_INCDIR}/../"

inherit autotools pkgconfig distutils-base

do_configure_prepend() {
    install -m 0644 ${WORKDIR}/acinclude.m4 ${S}/
}

do_configure_prepend() {
	install -m 0644 ${WORKDIR}/acinclude.m4 ${S}/
}

require fix-path.inc

FILES_${PN}-dev += "${libdir}/pygtk/2.0 ${bindir}/pygtk-*"

do_stage() {
	autotools_stage_includes
	sed -i s:/usr/share:${STAGING_DATADIR}: codegen/pygtk-codegen-2.0
	install -m 0755 codegen/pygtk-codegen-2.0 ${STAGING_BINDIR_NATIVE}/
	install -d ${STAGING_DATADIR}/pygtk/2.0/codegen
	install -d ${STAGING_DATADIR}/pygtk/2.0/defs/
	cp -pPr codegen/*.py* ${STAGING_DATADIR}/pygtk/2.0/codegen/
	cp -pPr *.defs ${STAGING_DATADIR}/pygtk/2.0/defs/
	cp -pPr gtk/*.defs ${STAGING_DATADIR}/pygtk/2.0/defs/
}
