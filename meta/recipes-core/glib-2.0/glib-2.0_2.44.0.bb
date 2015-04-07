require glib.inc

PE = "1"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://fix-conflicting-rand.patch \
           file://add-march-i486-into-CFLAGS-automatically.patch \
           file://glib-2.0-configure-readlink.patch \
           file://run-ptest \
           file://ptest-paths.patch \
           file://uclibc.patch \
           file://0001-configure.ac-Do-not-use-readlink-when-cross-compilin.patch \
           file://allow-run-media-sdX-drive-mount-if-username-root.patch \
           file://0001-GListModel-roll-back-use-of-type-redefinition.patch \
          "

SRC_URI_append_class-native = " file://glib-gettextize-dir.patch"

SRC_URI[md5sum] = "74cf7b4ea200b76e42a4c22c8daf0f93"
SRC_URI[sha256sum] = "f2d362b106a08fa801770d41829a06fcfe287a00421018869eebf5efc796f5b9"
