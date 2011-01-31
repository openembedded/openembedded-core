DESCRIPTION = "Library for lowlevel pixel operations"

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style & Public Domain"
LIC_FILES_CHKSUM = "file://COPYING;md5=fea989a44fb012b5e79584972a863d22 \
                    file://pixman/pixman-matrix.c;endline=25;md5=ba6e8769bfaaee2c41698755af04c4be \
                    file://pixman/pixman-arm-neon-asm.h;endline=24;md5=9a9cc1e51abbf1da58f4d9528ec9d49b \
                    file://pixman/pixman-x64-mmx-emulation.h;beginline=4;endline=9;md5=4e32716f2efaa6c4659222667c339bb8"

DEPENDS = "virtual/libx11"

PE = "1"
PR = "r0"

EXTRA_OECONF="--disable-gtk"

SRC_URI[md5sum] = "7ed10a0f0fde6853613105a99735e7e7"
SRC_URI[sha256sum] = "30316f269a6b84c32b47545ca7fc428fd16875849fea644dc6910b3f8c33f4a9"

