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

PR = "r0"

SRC_URI = "http://www.freedesktop.org/software/harfbuzz/release/harfbuzz-${PV}.tar.bz2 \
           file://disable_graphite.patch \
"

SRC_URI[md5sum] = "c48827713e93539dc7285f9e86ffbdc5"
SRC_URI[sha256sum] = "a4773003512035cb5c559de23e1d53f292bcb0212f023b540c4dc695b39690ed"

inherit autotools pkgconfig

DEPENDS = "icu glib-2.0 cairo freetype"

BBCLASSEXTEND = "native"
