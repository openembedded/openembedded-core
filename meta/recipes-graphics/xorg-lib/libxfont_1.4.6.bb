SUMMARY = "XFont: X Font rasterisation library"

DESCRIPTION = "libXfont provides various services for X servers, most \
notably font selection and rasterisation (through external libraries \
such as freetype)."

require xorg-lib-common.inc

LICENSE= "MIT & MIT-style & BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=a46c8040f2f737bcd0c435feb2ab1c2c"

DEPENDS += "freetype xtrans fontsproto libfontenc zlib"
PROVIDES = "xfont"

PE = "1"

XORG_PN = "libXfont"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "351a9b7348d165029bda52c9fdcb5c7a"
SRC_URI[sha256sum] = "d0cbfe4554dc17ceea413cdad5601d35ed8d05d5b880e60931a8775fd1157e9f"
