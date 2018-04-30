require glib.inc

PE = "1"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://run-ptest \
           file://uclibc_musl_translation.patch \
           file://allow-run-media-sdX-drive-mount-if-username-root.patch \
           file://0001-Remove-the-warning-about-deprecated-paths-in-schemas.patch \
           file://Enable-more-tests-while-cross-compiling.patch \
           file://0001-Install-gio-querymodules-as-libexec_PROGRAM.patch \
           file://0001-Do-not-ignore-return-value-of-write.patch \
           file://0001-Test-for-pthread_getname_np-before-using-it.patch \
           file://0010-Do-not-hardcode-python-path-into-various-tools.patch \
           file://0001-glib-mkenums-Ignore-other-per-value-options-than-ski.patch \
           "

SRC_URI_append_class-native = " file://relocate-modules.patch"

SRC_URI[md5sum] = "988af38524804ea1ae6bc9a2bad181ff"
SRC_URI[sha256sum] = "40ef3f44f2c651c7a31aedee44259809b6f03d3d20be44545cd7d177221c0b8d"
