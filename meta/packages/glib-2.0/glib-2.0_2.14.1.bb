require glib.inc

PR = "r1"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/glib/2.14/glib-${PV}.tar.bz2 \
           file://glib-checksum.diff;patch=1 \
           file://glibconfig-sysdefs.h \
           file://configure-libtool.patch;patch=1"
