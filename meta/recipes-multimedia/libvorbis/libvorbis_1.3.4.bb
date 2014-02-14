SUMMARY = "Ogg Vorbis Audio Codec"
DESCRIPTION = "Ogg Vorbis is a high-quality lossy audio codec \
that is free of intellectual property restrictions. libvorbis \
is the main vorbis codec library."
HOMEPAGE = "http://www.vorbis.com/"
BUGTRACKER = "https://trac.xiph.org"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=ca77c6c3ea4d29cb68dce8ef5ab0d897 \
                    file://include/vorbis/vorbisenc.h;beginline=1;endline=11;md5=d1c1d138863d6315131193d4046d81cb"
DEPENDS = "libogg"

SRC_URI = "http://downloads.xiph.org/releases/vorbis/${BP}.tar.xz"

SRC_URI[md5sum] = "55f2288055e44754275a17c9a2497391"
SRC_URI[sha256sum] = "2f05497d29195dc23ee952a24ee3973a74e6277569c4c2eca0ec5968e541f372"

inherit autotools pkgconfig

# vorbisfile.c reveals a problem in the gcc register spilling for the
# thumb instruction set...
FULL_OPTIMIZATION_thumb = "-O0"

EXTRA_OECONF = "--with-ogg-libraries=${STAGING_LIBDIR} \
                --with-ogg-includes=${STAGING_INCDIR}"
