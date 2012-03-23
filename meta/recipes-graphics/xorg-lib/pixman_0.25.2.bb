SUMMARY = "Pixman: Pixel Manipulation library"

DESCRIPTION = "Pixman provides a library for manipulating pixel regions \
-- a set of Y-X banded rectangles, image compositing using the \
Porter/Duff model and implicit mask generation for geometric primitives \
including trapezoids, triangles, and rectangles."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style & PD"
LIC_FILES_CHKSUM = "file://COPYING;md5=14096c769ae0cbb5fcb94ec468be11b3 \
                    file://pixman/pixman-matrix.c;endline=25;md5=ba6e8769bfaaee2c41698755af04c4be \
                    file://pixman/pixman-arm-neon-asm.h;endline=24;md5=9a9cc1e51abbf1da58f4d9528ec9d49b \
                   "
DEPENDS += "zlib libpng"

PR = "r1"

PE = "1"

IWMMXT = "--disable-arm-iwmmxt"

EXTRA_OECONF="--disable-gtk ${IWMMXT}"

SRC_URI[md5sum] = "8d5722f6f61db50034303947a40f5e7b"
SRC_URI[sha256sum] = "06d83ce5a5f2f8ab3761e88a2de1576b6596bb436190166a242b9d75a68bc1d8"
