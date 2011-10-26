SUMMARY = "High level language (GLib) binding for D-Bus"
DESCRIPTION = "GLib bindings for the D-Bus message bus that integrate \
the D-Bus library with the GLib thread abstraction and main loop."
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
LICENSE = "AFL-2 | GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=cf5b3a2f7083750d504333114e738656 \
                    file://dbus/dbus-glib.h;firstline=7;endline=21;md5=833ef01806b4c524fb3678a16300536f"
SECTION = "base"

DEPENDS = "expat glib-2.0 virtual/libintl dbus-glib-native dbus"
DEPENDS_virtclass-native = "glib-2.0-native dbus-native"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus-glib/dbus-glib-${PV}.tar.gz \
           file://no-examples.patch"

inherit autotools pkgconfig gettext

EXTRA_OECONF = "--with-introspect-xml=${STAGING_DATADIR_NATIVE}/dbus/dbus-bus-introspect.xml \
                --with-dbus-binding-tool=${STAGING_BINDIR_NATIVE}/dbus-binding-tool"
EXTRA_OECONF_virtclass-native = "--with-introspect-xml=${STAGING_DATADIR_NATIVE}/dbus/dbus-bus-introspect.xml"

FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-bash_completion = "${sysconfdir}/bash_completion.d/dbus-bash-completion.sh \
   ${libexecdir}/dbus-bash-completion-helper"
FILES_${PN}-dev += "${libdir}/dbus-1.0/include ${bindir}/dbus-glib-tool"
FILES_${PN}-dev += "${bindir}/dbus-binding-tool"

BBCLASSEXTEND = "native"
