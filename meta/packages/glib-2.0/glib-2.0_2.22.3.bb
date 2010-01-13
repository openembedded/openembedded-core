require glib.inc

PR = "r1"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/glib/2.22/glib-${PV}.tar.bz2 \
           file://glibconfig-sysdefs.h \
           file://configure-libtool.patch;patch=1"

SRC_URI_append_virtclass-native = " file://glib-gettextize-dir.patch;patch=1"

BBCLASSEXTEND = "native"
