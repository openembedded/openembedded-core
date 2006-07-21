SECTION = "x11/utils"
DEPENDS = "gtk+ glib-2.0 dbus libxml2 popt"
DESCRIPTION = "Settings daemon using DBUS for communication."
LICENSE = "GPL"
MAINTAINER = "Florian Boor <florian@kernelconcepts.de>"
PROVIDES = "gconf"
RPROVIDES_${PN} = "gconf"
RPROVIDES_${PN}-dev = "gconf-dev"

PV = "0.0+cvs${SRCDATE}"
PR = "r8"

SRC_URI = "cvs://anonymous@anoncvs.gnome.org/cvs/gnome;module=gconf;tag=gconf-dbus-2-6 \
           file://gconf-dbus-update.patch;patch=1;pnum=0 \
	   file://69gconfd-dbus"

inherit pkgconfig autotools
S = "${WORKDIR}/gconf"

FILES_${PN} += " ${libdir}/GConf/2/*.so ${libdir}/dbus-1.0 ${sysconfdir} ${datadir}/dbus*"

EXTRA_OECONF = " --with-ipc=dbus --disable-gtk-doc --enable-gtk --host=${HOST_SYS} --enable-shared --disable-static"

HEADERS = "gconf.h gconf-changeset.h gconf-listeners.h gconf-schema.h gconf-value.h gconf-error.h gconf-engine.h gconf-client.h gconf-enum-types.h"

do_stage() {
        oe_libinstall -so -C gconf libgconf-2 ${STAGING_LIBDIR}
        install -d ${STAGING_INCDIR}/gconf/2/gconf/
        ( cd gconf; for i in ${HEADERS}; do install -m 0644 $i ${STAGING_INCDIR}/gconf/2/gconf/$i; done )
        install -m 0644 gconf.m4 ${STAGING_DATADIR}/aclocal/gconf-2.m4
}

do_install_append () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d
	install -m 755 ${WORKDIR}/69gconfd-dbus ${D}/${sysconfdir}/X11/Xsession.d/
	install -d ${D}/${datadir}/dbus-1.0/services/
	install -m 644  gconf/gconf.service ${D}${datadir}/dbus-1.0/services/ 
}
