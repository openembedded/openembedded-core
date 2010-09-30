HOMEPAGE = "http://www.packagekit.org/"
DEPENDS = "polkit dbus-glib gconf gtk+"
LICENSE = "LGPLv2+"

SRC_URI = "http://hal.freedesktop.org/releases/polkit-gnome-${PV}.tar.bz2 \
          "

EXTRA_OECONF = " --disable-scrollkeeper \
                 --disable-man-pages \
                 --disable-examples \
                 --disable-gtk-doc \
                 --disable-introspection "

inherit autotools pkgconfig

FILES_${PN} += " ${datadir}/dbus-1 \
                 ${datadir}/PolicyKit \
               "

