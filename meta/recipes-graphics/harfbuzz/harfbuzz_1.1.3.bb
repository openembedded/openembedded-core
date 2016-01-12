SUMMARY = "Text shaping library"
DESCRIPTION = "HarfBuzz is an OpenType text shaping engine."
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/HarfBuzz"
BUGTRACKER = "https://bugs.freedesktop.org/enter_bug.cgi?product=HarfBuzz"
SECTION = "libs"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=e021dd6dda6ff1e6b1044002fc662b9b \
                    file://src/hb-ucdn/COPYING;md5=994ba0f1295f15b4bda4999a5bbeddef \
"

DEPENDS = "glib-2.0 cairo freetype"

SRC_URI = "http://www.freedesktop.org/software/harfbuzz/release/${BP}.tar.bz2"

SRC_URI[md5sum] = "671daf05153d57258e5cb992aa28c64a"
SRC_URI[sha256sum] = "d93d7cb7979c32672e902fdfa884599e63f07f2fa5b06c66147d20c516d4b8f7"

inherit autotools pkgconfig lib_package

PACKAGECONFIG ??= "icu"
PACKAGECONFIG[icu] = "--with-icu,--without-icu,icu"

EXTRA_OECONF = "--with-glib --with-freetype --with-cairo --without-graphite2"

PACKAGES =+ "${PN}-icu ${PN}-icu-dev"

FILES_${PN}-icu = "${libdir}/libharfbuzz-icu.so.*"
FILES_${PN}-icu-dev = "${libdir}/libharfbuzz-icu.la \
                       ${libdir}/libharfbuzz-icu.so \
                       ${libdir}/pkgconfig/harfbuzz-icu.pc \
"

BBCLASSEXTEND = "native"
