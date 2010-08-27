require xorg-app-common.inc

DESCRIPTION = "a program to create an index of scalable font files for X"

DEPENDS += " zlib libfontenc freetype virtual/libx11"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=8ecbbbc1259a329e96ccc4dd86ad2ca2"
