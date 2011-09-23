DESCRIPTION = "Settings daemon using DBUS for communication."
SECTION = "x11/utils"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"

DEPENDS = "intltool-native virtual/libintl glib-2.0 dbus dbus-glib libxml2 popt gtk-doc-native"
PROVIDES = "gconf"
RPROVIDES_${PN} = "gconf"
RPROVIDES_${PN}-dev = "gconf-dev"

#SRCREV = "705"
#PV = "2.16.0+svnr${SRCPV}"
PR = "r1"

# This SVN repo is no longer available use a tarball mirror site until
# we move to proper gconf recipe.
#SRC_URI = "svn://developer.imendio.com/svn/gconf-dbus;module=trunk;proto=http"
SRC_URI = "http://autobuilder.pokylinux.org/sources/trunk_developer.imendio.com_.svn.gconf-dbus_705_.tar.gz"

S = "${WORKDIR}/trunk"

inherit pkgconfig autotools

PARALLEL_MAKE = ""


EXTRA_OECONF = "--disable-gtk-doc --disable-gtk --enable-shared --disable-static --enable-debug=yes"


do_configure_prepend() {
        touch gtk-doc.make
}

FILES_${PN} = "${libdir}/GConf-dbus/2/*.so ${libdir}/dbus-1.0 ${sysconfdir} ${datadir}/dbus* ${libdir}/*.so.* ${bindir}/* ${libexecdir}/*"
FILES_${PN}-dbg += " ${libdir}/GConf-dbus/2/.debug"
FILES_${PN}-dev += "${datadir}/sgml/gconf/gconf-1.0.dtd \
		    ${libdir}/GConf-dbus/2/*.la"

BBCLASSEXTEND = "native"

