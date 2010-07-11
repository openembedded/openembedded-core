DESCRIPTION = "high level language (glib) binding for dbus"
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
LICENSE = "AFL2.1 | GPLv2+"
SECTION = "base"
DEPENDS = "expat glib-2.0 virtual/libintl dbus-glib-native dbus"
DEPENDS_virtclass-native = "glib-2.0-native dbus-native"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus-glib/dbus-glib-${PV}.tar.gz \
           file://no-examples.patch"

inherit autotools pkgconfig gettext

FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev += "${libdir}/dbus-1.0/include ${bindir}/dbus-glib-tool"
FILES_${PN}-dev += "${bindir}/dbus-binding-tool"

EXTRA_OECONF = "--with-introspect-xml=${STAGING_DATADIR_NATIVE}/dbus/dbus-bus-introspect.xml \
                --with-dbus-binding-tool=${STAGING_BINDIR_NATIVE}/dbus-binding-tool"
EXTRA_OECONF_virtclass-native = "--with-introspect-xml=${STAGING_DATADIR_NATIVE}/dbus/dbus-bus-introspect.xml"

BBCLASSEXTEND = "native"
