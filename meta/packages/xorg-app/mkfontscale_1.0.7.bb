require xorg-app-common.inc

DESCRIPTION = "a program to create an index of scalable font files for X"

DEPENDS += " zlib libfontenc freetype virtual/libx11"

BBCLASSEXTEND = "native"