require glib.inc

PE = "1"
PR = "r0"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/glib/2.26/glib-${PV}.tar.bz2 \
           file://configure-libtool.patch \
           file://60_wait-longer-for-threads-to-die.patch \
           file://g_once_init_enter.patch \
          "

SRC_URI[md5sum] = "17535accceef55bcb17a74d73f9c2aef"
SRC_URI[sha256sum] = "7a74ff12b6b9dee1f2d0e520b56b68b621920c4f4250bdf23468e515625c28d5"

SRC_URI_append_virtclass-native = " file://glib-gettextize-dir.patch"
BBCLASSEXTEND = "native"
