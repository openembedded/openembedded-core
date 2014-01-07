SUMMARY = "XFont: X Font rasterisation library"

DESCRIPTION = "libXfont provides various services for X servers, most \
notably font selection and rasterisation (through external libraries \
such as freetype)."

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=a46c8040f2f737bcd0c435feb2ab1c2c"

DEPENDS += "freetype xtrans fontsproto libfontenc zlib"
PROVIDES = "xfont"

PE = "1"

XORG_PN = "libXfont"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "b21ee5739d5d2e5028b302fbf9fe630b"
SRC_URI[sha256sum] = "d16ea3541835d296b19cfb05d7e64fc62173d8e7eb93284402ec761b951d1543"
