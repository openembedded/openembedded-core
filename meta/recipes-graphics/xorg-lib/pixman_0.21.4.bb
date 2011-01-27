DESCRIPTION = "Library for lowlevel pixel operations"

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style & Public Domain"
LIC_FILES_CHKSUM = "file://COPYING;md5=14096c769ae0cbb5fcb94ec468be11b3 \
                    file://pixman/pixman-matrix.c;endline=25;md5=ba6e8769bfaaee2c41698755af04c4be \
                    file://pixman/pixman-arm-neon-asm.h;endline=24;md5=9a9cc1e51abbf1da58f4d9528ec9d49b \
                    file://pixman/pixman-x64-mmx-emulation.h;beginline=4;endline=9;md5=4e32716f2efaa6c4659222667c339bb8"

DEPENDS = "virtual/libx11"

PR = "r0"

EXTRA_OECONF="--disable-gtk"

SRC_URI[md5sum] = "ae57f4fa9121ab9895b9789b2e81f1e0"
SRC_URI[sha256sum] = "7809f8aa7dcd99bc0e3a12eef65266d34e1f2988df4c814e5f747ddceed22ddf"

