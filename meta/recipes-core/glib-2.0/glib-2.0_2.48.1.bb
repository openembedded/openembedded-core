require glib.inc

PE = "1"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz \
           file://configure-libtool.patch \
           file://fix-conflicting-rand.patch \
           file://run-ptest \
           file://ptest-paths.patch \
           file://uclibc_musl_translation.patch \
           file://allow-run-media-sdX-drive-mount-if-username-root.patch \
           file://0001-Remove-the-warning-about-deprecated-paths-in-schemas.patch \
           file://Enable-more-tests-while-cross-compiling.patch \
           file://gi-exclude.patch \
           file://0001-Install-gio-querymodules-as-libexec_PROGRAM.patch \
           file://0001-Do-not-ignore-return-value-of-write.patch \
           file://0002-tests-Ignore-y2k-warnings.patch \
           "

SRC_URI_append_class-native = " file://glib-gettextize-dir.patch \
                                file://relocate-modules.patch"

SRC_URI[md5sum] = "67bd3b75c9f6d5587b457dc01cdcd5bb"
SRC_URI[sha256sum] = "74411bff489cb2a3527bac743a51018841a56a4d896cc1e0d0d54f8166a14612"
