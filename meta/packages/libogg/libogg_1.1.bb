SECTION = "libs"
DESCRIPTION = "libogg is the bitstream and framing library \
for the Ogg project. It provides functions which are \
necessary to codec libraries like libvorbis."
LICENSE = "BSD"
PR = "r4"

SRC_URI = "http://www.vorbis.com/files/1.0.1/unix/libogg-${PV}.tar.gz \
file://m4.patch;patch=1"

inherit autotools pkgconfig

do_stage () {
        autotools_stage_all
}
