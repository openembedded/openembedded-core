SUMMARY = "Text shaping library"
DESCRIPTION = "HarfBuzz is an OpenType text shaping engine."
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/HarfBuzz"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=HarfBuzz"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=e021dd6dda6ff1e6b1044002fc662b9b \
                    file://src/hb-ucdn/COPYING;md5=994ba0f1295f15b4bda4999a5bbeddef \
"

SECTION = "libs"

SRC_URI = "http://www.freedesktop.org/software/harfbuzz/release/harfbuzz-${PV}.tar.bz2 \
"

SRC_URI[md5sum] = "d3c1bcd7073cbca29fea37fab50ded7d"
SRC_URI[sha256sum] = "989680807e76197418338e44f226e02f155f33031efd9aff14dbc4dc14af71da"

inherit autotools pkgconfig

DEPENDS = "icu glib-2.0 cairo freetype"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--with-glib --with-freetype --with-cairo --with-icu --without-graphite2"

PACKAGES =+ "${PN}-icu ${PN}-icu-dbg ${PN}-icu-dev"

FILES_${PN}-icu = "${libdir}/libharfbuzz-icu.so.*"
FILES_${PN}-icu-dbg = "${libdir}/.debug/libharfbuzz-icu.so*"
FILES_${PN}-icu-dev = "${libdir}/libharfbuzz-icu.la \
                       ${libdir}/libharfbuzz-icu.so \
                       ${libdir}/pkgconfig/harfbuzz-icu.pc \
"
