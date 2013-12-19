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

SRC_URI[md5sum] = "491b631239f51dcd4f7934775306c4e7"
SRC_URI[sha256sum] = "dc6e5997a569526cd28147a80a0f65466e87ae617753b38704a60184bc6d6bee"

inherit autotools pkgconfig

DEPENDS = "glib-2.0 cairo freetype"

BBCLASSEXTEND = "native"

EXTRA_OECONF = "--with-glib --with-freetype --with-cairo --without-graphite2"

PACKAGECONFIG ??= ""
PACKAGECONFIG[icu] = "--with-icu,--without-icu,icu"

PACKAGES =+ "${PN}-icu ${PN}-icu-dbg ${PN}-icu-dev"

FILES_${PN}-icu = "${libdir}/libharfbuzz-icu.so.*"
FILES_${PN}-icu-dbg = "${libdir}/.debug/libharfbuzz-icu.so*"
FILES_${PN}-icu-dev = "${libdir}/libharfbuzz-icu.la \
                       ${libdir}/libharfbuzz-icu.so \
                       ${libdir}/pkgconfig/harfbuzz-icu.pc \
"
