SUMMARY = "Ogg bitstream and framing libary"
DESCRIPTION = "libogg is the bitstream and framing library \
for the Ogg project. It provides functions which are \
necessary to codec libraries like libvorbis."
HOMEPAGE = "http://xiph.org/"
BUGTRACKER = "https://trac.xiph.org/newticket"
SECTION = "libs"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=db1b7a668b2a6f47b2af88fb008ad555 \
                    file://include/ogg/ogg.h;beginline=1;endline=11;md5=eda812856f13a3b1326eb8f020cc3b0b"

PR = "r0"

SRC_URI = "http://downloads.xiph.org/releases/ogg/libogg-${PV}.tar.gz"

SRC_URI[md5sum] = "b998c2420721146df3b3d0e7776c97b9"
SRC_URI[sha256sum] = "6858848617bca6eab01e7d8526bc0d2a417e95070a255cbf9c881881365e36c0"

inherit autotools pkgconfig
