LICENSE = "LGPL"
SECTION = "x11/gnome"
DESCRIPTION = "GNOME configuration database system"
SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/GConf/2.6/GConf-${PV}.tar.bz2 \
	   file://backends.patch;patch=1"
DEPENDS = "orbit2 glib-2.0  libxml2"
ORBIT_IDL_SRC = "${STAGING_BINDIR}/orbit-idl-2"

FILES_${PN} += " ${libdir}/GConf/*"

S = "${WORKDIR}/GConf-${PV}"

EXTRA_OECONF = "--disable-gtk-doc"

inherit autotools pkgconfig gettext

HEADERS = "gconf.h gconf-changeset.h gconf-listeners.h gconf-schema.h gconf-value.h gconf-error.h gconf-engine.h gconf-client.h gconf-enum-types.h"

do_compile() {
	oe_runmake ORBIT_IDL="${ORBIT_IDL_SRC}"
}

do_stage() {
	oe_libinstall -so -C gconf libgconf-2 ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/gconf/2/gconf/
	( cd gconf; for i in ${HEADERS}; do install -m 0644 $i ${STAGING_INCDIR}/gconf/2/gconf/$i; done )
	install -m 0644 gconf.m4 ${STAGING_DATADIR}/aclocal/gconf-2.m4
}

do_install() {
	oe_runmake ORBIT_IDL="${ORBIT_IDL_SRC}" DESTDIR="${D}" install
}
