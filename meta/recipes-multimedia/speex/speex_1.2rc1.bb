SUMMARY = "Speech Audio Codec"
DESCRIPTION = "Speex is an Open Source/Free Software patent-free audio compression format designed for speech."
HOMEPAGE = "http://www.speex.org"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=314649d8ba9dd7045dfb6683f298d0a8 \
                    file://include/speex/speex.h;beginline=1;endline=34;md5=a68129f78d7fe66e07163f73aba143b3"
DEPENDS = "libogg"

PR = "r2"

SRC_URI = "http://downloads.us.xiph.org/releases/speex/speex-${PV}.tar.gz"

SRC_URI[md5sum] = "c4438b22c08e5811ff10e2b06ee9b9ae"
SRC_URI[sha256sum] = "342f30dc57bd4a6dad41398365baaa690429660b10d866b7d508e8f1179cb7a6"

PARALLEL_MAKE = ""

inherit autotools pkgconfig lib_package

EXTRA_OECONF = " --enable-fixed-point --with-ogg-libraries=${STAGING_LIBDIR} \
                 --with-ogg-includes=${STAGING_INCDIR} --disable-oggtest"

require speex-fpu.inc
EXTRA_OECONF += "${@get_speex_fpu_setting(bb, d)}"
