SUMMARY = "Multitouch Protocol Translation Library"

DESCRIPTION = "mtdev is a library which transforms all variants of kernel \
multitouch events to the slotted type B protocol. The events put into mtdev may \
be from any MT device, specifically type A without contact tracking, type A with \
contact tracking, or type B with contact tracking"

HOMEPAGE = "http://bitmath.org/code/mtdev/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea6bd0268bb0fcd6b27698616ceee5d6"

SRC_URI = "http://bitmath.org/code/${BPN}/${BP}.tar.bz2 \
           file://fixsepbuild.patch"
SRC_URI[md5sum] = "73a915d6075b31db4f08ab8c6d314695"
SRC_URI[sha256sum] = "6b59b055ff22f2b91d4284187c2515826c338b81f6f33bd90f6bedc7c1fb9a38"

inherit autotools pkgconfig
