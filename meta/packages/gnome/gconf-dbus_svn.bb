SECTION = "x11/utils"
DEPENDS = "intltool-native glib-2.0 dbus dbus-glib libxml2 popt gtk-doc-native"
DESCRIPTION = "Settings daemon using DBUS for communication."
LICENSE = "GPL"
PROVIDES = "gconf"
RPROVIDES_${PN} = "gconf"
RPROVIDES_${PN}-dev = "gconf-dev"

PV = "2.16.0+svnr${SRCREV}"
PR = "r3"

SRC_URI = "svn://developer.imendio.com/svn/gconf-dbus;module=trunk;proto=http \
	   file://69gconfd-dbus"

inherit pkgconfig autotools 
S = "${WORKDIR}/trunk"

PARALLEL_MAKE = ""

FILES_${PN} = "${libdir}/GConf-dbus/2/*.so ${libdir}/dbus-1.0 ${sysconfdir} ${datadir}/dbus* ${libdir}/*.so.* ${bindir}/* ${libexecdir}/*"
FILES_${PN}-dbg += " ${libdir}/GConf-dbus/2/.debug"

EXTRA_OECONF = " --with-ipc=dbus --disable-gtk-doc --disable-gtk --enable-shared --disable-static"

HEADERS = "gconf.h gconf-changeset.h gconf-listeners.h gconf-schema.h gconf-value.h gconf-error.h gconf-engine.h gconf-client.h gconf-enum-types.h"

do_configure_prepend() {
        touch gtk-doc.make
}

do_stage() {
        autotools_stage_all
        install -m 0644 gconf-2.m4 ${STAGING_DATADIR}/aclocal/gconf-2.m4
}

do_install_append () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d
	install -m 755 ${WORKDIR}/69gconfd-dbus ${D}/${sysconfdir}/X11/Xsession.d/
}
