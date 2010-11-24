SUMMARY = "Ogg Vorbis Audio Codec"
DESCRIPTION = "Ogg Vorbis is a high-quality lossy audio codec \
that is free of intellectual property restrictions. libvorbis \
is the main vorbis codec library."
HOMEPAGE = "http://xiph.org/"
BUGTRACKER = "https://trac.xiph.org/newticket"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=ca77c6c3ea4d29cb68dce8ef5ab0d897 \
                    file://include/vorbis/vorbisenc.h;beginline=1;endline=11;md5=d1c1d138863d6315131193d4046d81cb"
DEPENDS = "libogg"

PR = "r0"

SRC_URI = "http://downloads.xiph.org/releases/vorbis/libvorbis-${PV}.tar.gz"

SRC_URI[md5sum] = "c870b9bd5858a0ecb5275c14486d9554"
SRC_URI[sha256sum] = "eeb4dcada143846dfba760d982954a02f82e08845cbc33871f5dac547b8b6124"

inherit autotools pkgconfig

# vorbisfile.c reveals a problem in the gcc register spilling for the
# thumb instruction set...
FULL_OPTIMIZATION_thumb = "-O0"

EXTRA_OECONF = "--with-ogg-libraries=${STAGING_LIBDIR} \
                --with-ogg-includes=${STAGING_INCDIR}"
