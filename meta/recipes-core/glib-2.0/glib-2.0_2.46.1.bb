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
           file://uclibc_musl_translation.patch \
           file://0001-configure.ac-Do-not-use-readlink-when-cross-compilin.patch \
           file://allow-run-media-sdX-drive-mount-if-username-root.patch \
	   file://0001-Remove-the-warning-about-deprecated-paths-in-schemas.patch \
	   file://0001-gio-tests-Don-t-depend-on-a-data-file-that-s-not-bui.patch \
           file://Enable-more-tests-while-cross-compiling.patch \
          "

SRC_URI_append_class-native = " file://glib-gettextize-dir.patch"

SRC_URI[md5sum] = "c90e93ceb45100ffc1d40ec5d2ca3248"
SRC_URI[sha256sum] = "5a1f03b952ebc3a7e9f612b8724f70898183e31503db329b4f15d07163c8fdfb"
