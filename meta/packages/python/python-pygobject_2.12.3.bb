DESCRIPTION = "Python GObject bindings"
SECTION = "devel/python"
LICENSE = "LGPL"
DEPENDS = "python-pygobject-native"
PR = "r3"

SRC_URI = "ftp://ftp.gnome.org/pub/GNOME/sources/pygobject/2.12/pygobject-${PV}.tar.bz2 \
           file://python-path.patch;patch=1"
S = "${WORKDIR}/pygobject-${PV}"

inherit autotools distutils-base pkgconfig

EXTRA_OECONF += "--with-python-includes=${STAGING_INCDIR}/../"

do_stage() {
	autotools_stage_all
	install -d ${STAGING_LIBDIR}/../share/pygobject/
	cp -dpfR docs/* ${STAGING_LIBDIR}/../share/pygobject/
	install -d ${STAGING_LIBDIR}/../share/gtk-doc/html/pygobject/
	cp docs/style.css ${STAGING_LIBDIR}/../share/gtk-doc/html/pygobject/
}

FILES_${PN} = "${libdir}/python*"
FILES_${PN}-dev += "${datadir}/pygobject/xsl"
