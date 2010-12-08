require glib.inc

PR = "r0"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/glib/2.27/glib-${PV}.tar.bz2 \
           file://configure-libtool.patch \
           file://60_wait-longer-for-threads-to-die.patch"

SRC_URI[md5sum] = "d3e976ff92b55b6064a0eb3110f36158"
SRC_URI[sha256sum] = "c44177b635e88639361eb1daf7aaa33315a00aaf46db5accf2f11920c7ff1919"

SRC_URI_append_virtclass-native = " file://glib-gettextize-dir.patch"
BBCLASSEXTEND = "native"
