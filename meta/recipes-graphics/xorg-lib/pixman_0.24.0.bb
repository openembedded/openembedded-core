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

PE = "1"
PR = "r0"

EXTRA_OECONF="--disable-gtk"

SRC_URI[md5sum] = "db5ffcd50064421176e8afb7b85fd1a7"
SRC_URI[sha256sum] = "744e8b36b29ba76bdfe8f7cbc45585fb237533ac8d716a27e306874dcfa1cf44"

