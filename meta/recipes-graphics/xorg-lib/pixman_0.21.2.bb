DESCRIPTION = "Library for lowlevel pixel operations"

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style & Public Domain"
LIC_FILES_CHKSUM = "file://COPYING;md5=71e682cc54eaaf251f96a9038423a5c3 \
                    file://pixman/pixman-matrix.c;endline=25;md5=ba6e8769bfaaee2c41698755af04c4be \
                    file://pixman/pixman-arm-neon-asm.h;endline=24;md5=9a9cc1e51abbf1da58f4d9528ec9d49b \
                    file://pixman/pixman-x64-mmx-emulation.h;beginline=4;endline=9;md5=4e32716f2efaa6c4659222667c339bb8"

DEPENDS = "virtual/libx11"

PR = "r0"

EXTRA_OECONF="--disable-gtk"

SRC_URI[md5sum] = "4bc4cf052635265f7a98ad3e890ae329"
SRC_URI[sha256sum] = "7ea3601f6ab411645d73db2f37096f5979425d309784a865444ecfa18614d06d"

