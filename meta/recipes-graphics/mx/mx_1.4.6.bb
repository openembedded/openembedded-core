DESCRIPTION = "Clutter based widget library"
LICENSE = "LGPLv2.1"

PR = "r0"

DEPENDS = "clutter-1.8 dbus-glib libxrandr gdk-pixbuf startup-notification"

inherit autotools gettext

SRC_URI = "http://source.clutter-project.org/sources/mx/1.4/mx-${PV}.tar.bz2"

SRC_URI[md5sum] = "c7192ca7c43bb1a39adc2fafdc012f49"
SRC_URI[sha256sum] = "a3c7ffaf29b282144c982757b450d145cd85867183dfafef4750a00a0d406672"

LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=fbc093901857fcd118f065f900982c24 \
                    file://mx/mx-widget.c;beginline=8;endline=20;md5=13bba3c973a72414a701e1e87b5ee879"

EXTRA_OECONF = "--disable-introspection --disable-gtk-widgets --with-dbus"

