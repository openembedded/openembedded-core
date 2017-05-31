require glib.inc

PE = "1"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://run-ptest \
           file://ptest-paths.patch \
           file://uclibc_musl_translation.patch \
           file://allow-run-media-sdX-drive-mount-if-username-root.patch \
           file://0001-Remove-the-warning-about-deprecated-paths-in-schemas.patch \
           file://Enable-more-tests-while-cross-compiling.patch \
           file://0001-Install-gio-querymodules-as-libexec_PROGRAM.patch \
           file://0001-Do-not-ignore-return-value-of-write.patch \
           file://0001-Test-for-pthread_getname_np-before-using-it.patch \
           "

SRC_URI_append_class-native = " file://relocate-modules.patch"

SRC_URI[md5sum] = "ec099bce26ce6a85104ed1d89bb45856"
SRC_URI[sha256sum] = "f00e5d9e2a2948b1da25fcba734a6b7a40f556de8bc9f528a53f6569969ac5d0"
