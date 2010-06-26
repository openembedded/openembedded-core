SECTION = "libs"
DESCRIPTION = "libogg is the bitstream and framing library \
for the Ogg project. It provides functions which are \
necessary to codec libraries like libvorbis."
LICENSE = "BSD"
PR = "r0"

SRC_URI = "http://downloads.xiph.org/releases/ogg/libogg-${PV}.tar.gz"

inherit autotools pkgconfig
