require python-pygobject_${PV}.bb

DEPENDS = "python-native glib-2.0-native"

SRC_URI = "ftp://ftp.gnome.org/pub/GNOME/sources/pygobject/2.12/pygobject-${PV}.tar.bz2 "

inherit native
