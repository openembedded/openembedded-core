DESCRIPTION = "Clutter based widget library"
LICENSE = "LGPLv2.1"

PR = "r1"

DEPENDS = "clutter-1.8 dbus-glib libxrandr gdk-pixbuf startup-notification"

inherit autotools gettext

SRC_URI = "http://source.clutter-project.org/sources/mx/1.4/mx-${PV}.tar.bz2 \
	   file://introspection-m4.patch"

SRC_URI[md5sum] = "faf8d97ad9995f54cc91f90bc90c8f9d"
SRC_URI[sha256sum] = "9d40dd48a8e3d098cc75c05163f77305ffb83439783dc91be50681c9502660ce"

LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=fbc093901857fcd118f065f900982c24 \
                    file://mx/mx-widget.c;beginline=8;endline=20;md5=13bba3c973a72414a701e1e87b5ee879"

EXTRA_OECONF = "--disable-introspection --disable-gtk-widgets --with-dbus"

