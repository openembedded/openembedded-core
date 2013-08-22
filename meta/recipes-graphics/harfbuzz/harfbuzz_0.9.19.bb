SUMMARY = "Text shaping library"
DESCRIPTION = "HarfBuzz is an OpenType text shaping engine."
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/HarfBuzz"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=HarfBuzz"

LICENSE = "MIT & ICU"
LIC_FILES_CHKSUM = "file://COPYING;md5=e021dd6dda6ff1e6b1044002fc662b9b \
                    file://src/hb-ucdn/COPYING;md5=994ba0f1295f15b4bda4999a5bbeddef \
                    file://src/hb-icu-le/COPYING;md5=0ac60acf068e2cc9facdf86169a9c13e \
                    file://src/hb-icu-le/license.html;md5=9136737088bbfbbf86d9a714da49fb89 \
                    file://src/hb-old/COPYING;md5=6e8442c12d498ce55cfe39fc60f97981 \
"

SECTION = "libs"

SRC_URI = "http://www.freedesktop.org/software/harfbuzz/release/harfbuzz-${PV}.tar.bz2 \
"

SRC_URI[md5sum] = "9782581ee6ef972554772e84ca448131"
SRC_URI[sha256sum] = "d2da0f060d47f6ad9de8c8781bb21fa4b9eae8ea1cd1e956b814095baa002f35"

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
