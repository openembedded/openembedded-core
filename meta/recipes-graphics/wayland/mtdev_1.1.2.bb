inherit autotools pkgconfig

SUMMARY = "Multitouch Protocol Translation Library"

DESCRIPTION = " The mtdev is a stand-alone library which transforms all \
variants of kernel MT events to the slotted type B protocol. The events put \
into mtdev may be from any MT device, specifically type A without contact \
tracking, type A with contact tracking, or type B with contact tracking"

HOMEPAGE = "http://bitmath.org/code/mtdev/"
SECTION = "libs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=ea6bd0268bb0fcd6b27698616ceee5d6"

SRC_URI = "http://bitmath.org/code/mtdev/mtdev-1.1.2.tar.bz2 \
           file://fixsepbuild.patch"
SRC_URI[md5sum] = "d9c7700918fc392e29da7477ae20c5c2"
SRC_URI[sha256sum] = "698dd11ca08b3a73ad33d8a5076f6d9e018d97afad3eba36f540b8ea7b775a30"

PR = "r0"
