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

SRC_URI[md5sum] = "5a9fcabc9a1b7c6f1cd75ddc78f36c56"
SRC_URI[sha256sum] = "ab000574bc26d5f01284f5b0f50e12dc761d035c429f2e9c70cb2a9487d8cfba"

inherit autotools pkgconfig
