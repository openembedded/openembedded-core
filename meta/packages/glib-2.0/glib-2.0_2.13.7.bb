require glib.inc

PR = "r1"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/glib/2.13/glib-${PV}.tar.bz2 \
           file://glibconfig-sysdefs.h \
           file://configure-libtool.patch;patch=1 \
           file://casts.patch;patch=1;pnum=0"
