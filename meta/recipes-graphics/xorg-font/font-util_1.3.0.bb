SUMMARY = "X.Org font package creation/installation utilities"

require xorg-font-common.inc

#Unicode is MIT
LICENSE = "BSD & MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=c0067273d90f6336008cb2504e14bd32 \
                    file://ucs2any.c;endline=28;md5=8357dc567fc628bd12696f15b2a33bcb \
                    file://bdftruncate.c;endline=26;md5=4f82ffc101a1b165eae9c6998abff937 \
                    file://map-ISO8859-1;beginline=9;endline=23;md5=1cecb984063248f29ffe5c46f5c04f34"

DEPENDS = "encodings util-macros"
DEPENDS_class-native = "util-macros-native"
RDEPENDS_${PN} = "mkfontdir mkfontscale encodings"
RDEPENDS_${PN}_class-native = "mkfontdir-native mkfontscale-native"

PR = "${INC_PR}.0"

do_configure_prepend() {
        sed -i "s#MAPFILES_PATH=\`pkg-config#MAPFILES_PATH=\`PKG_CONFIG_PATH=\"${STAGING_LIBDIR_NATIVE}/pkgconfig\" pkg-config#g" ${S}/fontutil.m4.in
}

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "ddfc8a89d597651408369d940d03d06b"
SRC_URI[sha256sum] = "dfa9e55625a4e0250f32fabab1fd5c8ffcd2d1ff2720d6fcf0f74bc8a5929195"
