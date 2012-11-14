DESCRIPTION = "Clutter based widget library"
LICENSE = "LGPLv2.1"

LIC_FILES_CHKSUM = "file://COPYING.LIB;md5=fbc093901857fcd118f065f900982c24 \
                    file://mx/mx-widget.c;beginline=8;endline=20;md5=13bba3c973a72414a701e1e87b5ee879"

PR = "r0"

DEPENDS = "clutter-1.8 dbus-glib libxrandr gdk-pixbuf startup-notification"

inherit autotools gettext

SRC_URI = "https://github.com/downloads/clutter-project/${BPN}/${BP}.tar.xz"

SRC_URI[md5sum] = "19b1e4918a5ae6d014fc0dab2bb3d0a1"
SRC_URI[sha256sum] = "1d2930d196717cacbee0ee101cf21d289b8200b5e938823d852b3b4a2f4a0e9d"

EXTRA_OECONF = "--disable-introspection --disable-gtk-widgets --with-dbus"
