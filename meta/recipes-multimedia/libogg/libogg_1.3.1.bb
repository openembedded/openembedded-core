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

SRC_URI = "http://downloads.xiph.org/releases/ogg/libogg-${PV}.tar.xz"

SRC_URI[md5sum] = "ca25d8da0ddfc8c6cbbf78d847a209fe"
SRC_URI[sha256sum] = "3a5bad78d81afb78908326d11761c0fb1a0662ee7150b6ad587cc586838cdcfa"

inherit autotools pkgconfig
