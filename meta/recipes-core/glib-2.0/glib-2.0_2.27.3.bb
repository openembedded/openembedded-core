require glib.inc

PR = "r0"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/glib/2.27/glib-${PV}.tar.bz2 \
           file://configure-libtool.patch \
           file://60_wait-longer-for-threads-to-die.patch"

SRC_URI_append_virtclass-native = " file://glib-gettextize-dir.patch"
BBCLASSEXTEND = "native"
