require xorg-app-common.inc

SUMMARY = "A program to create an index of scalable font files for X"

DESCRIPTION = "For each directory argument, mkfontscale reads all of the \
scalable font files in the directory. For every font file found, an X11 \
font name (XLFD) is generated, and is written together with the file \
name to a file fonts.scale in the directory.  The resulting fonts.scale \
is used by the mkfontdir program."

DEPENDS += " zlib libfontenc freetype virtual/libx11"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=2e0d129d05305176d1a790e0ac1acb7f"

SRC_URI[md5sum] = "414fcb053418fb1418e3a39f4a37e0f7"
SRC_URI[sha256sum] = "ce55f862679b8ec127d7f7315ac04a8d64a0d90a0309a70dc56c1ba3f9806994"
