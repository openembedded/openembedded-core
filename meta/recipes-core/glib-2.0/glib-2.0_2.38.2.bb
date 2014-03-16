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
           file://ptest-dbus.patch \
           file://ptest-paths.patch \
           file://gtest-skip-fixes.patch \
           file://gio-test-race.patch \
           file://uclibc.patch \
          "

SRC_URI_append_class-native = " file://glib-gettextize-dir.patch"

SRC_URI[md5sum] = "26d1d08e478fc48c181ca8be44f5b69f"
SRC_URI[sha256sum] = "056a9854c0966a0945e16146b3345b7a82562a5ba4d5516fd10398732aea5734"
