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

SRC_URI[md5sum] = "5210c9385c6cc4a00ce829d8dc9c819b"
SRC_URI[sha256sum] = "6eb57786cd79eebfbaca386fe24dcfe50689dbf433d052e58291c2925f2050f9"
