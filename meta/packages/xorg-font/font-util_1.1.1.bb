require xorg-font-common.inc

PACKAGE_ARCH = "${BASE_PACKAGE_ARCH}"

DESCRIPTION = "X font utils."

DEPENDS = "encodings util-macros"
DEPENDS_virtclass-native = "util-macros-native"
RDEPENDS = "mkfontdir mkfontscale encodings"

PR = "${INC_PR}.1"

do_configure_prepend() {
        sed -i "s#MAPFILES_PATH=\`pkg-config#MAPFILES_PATH=\`PKG_CONFIG_PATH=\"${STAGING_LIBDIR_NATIVE}/pkg-config\" pkg-config#g" fontutil.m4.in
}

BBCLASSEXTEND = "native"