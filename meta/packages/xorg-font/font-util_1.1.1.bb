DESCRIPTION = "X.Org font package creation/installation utilities"

require xorg-font-common.inc

LICENSE = "BSD & MIT-style & Unicode"
LIC_FILES_CHKSUM = "file://COPYING;md5=0b73d82c5f21398b013c07e8b4012376 \
                    file://ucs2any.c;endline=28;md5=8357dc567fc628bd12696f15b2a33bcb \
                    file://bdftruncate.c;endline=26;md5=4f82ffc101a1b165eae9c6998abff937 \
                    file://map-ISO8859-1;beginline=9;endline=23;md5=1cecb984063248f29ffe5c46f5c04f34"

PACKAGE_ARCH = "${BASE_PACKAGE_ARCH}"

DEPENDS = "encodings util-macros"
DEPENDS_virtclass-native = "util-macros-native"
RDEPENDS = "mkfontdir mkfontscale encodings"

PR = "${INC_PR}.1"

do_configure_prepend() {
        sed -i "s#MAPFILES_PATH=\`pkg-config#MAPFILES_PATH=\`PKG_CONFIG_PATH=\"${STAGING_LIBDIR_NATIVE}/pkg-config\" pkg-config#g" fontutil.m4.in
}

BBCLASSEXTEND = "native"
