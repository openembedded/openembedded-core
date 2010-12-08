require xorg-app-common.inc

DESCRIPTION = "a program to create an index of scalable font files for X"

DEPENDS += " zlib libfontenc freetype virtual/libx11"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=8ecbbbc1259a329e96ccc4dd86ad2ca2"

SRC_URI[md5sum] = "96ca346f185c0ab48e42bf5bb0375da5"
SRC_URI[sha256sum] = "8306b229cca233216a6582cb1ff60af78e37c47d6412ac823d7d41c3d7de7127"
