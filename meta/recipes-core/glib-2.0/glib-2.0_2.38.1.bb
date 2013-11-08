require glib.inc

PE = "1"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://fix-conflicting-rand.patch \
           file://add-march-i486-into-CFLAGS-automatically.patch \
           file://glib-2.0-configure-readlink.patch \
           file://run-ptest \
           file://0001-gio-Fix-Werror-format-string-errors-from-mismatched-.patch \
          "

SRC_URI_append_class-native = " file://glib-gettextize-dir.patch"

SRC_URI[md5sum] = "f3f6789151c1810f2fe23fe9ebb8b828"
SRC_URI[sha256sum] = "01906c62ac666d2ab3183cc07261b2536fab7b211c6129ab66b119c2af56d159"
