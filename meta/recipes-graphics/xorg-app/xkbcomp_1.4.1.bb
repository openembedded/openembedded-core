require xorg-app-common.inc

SUMMARY = "A program to compile XKB keyboard description"

DESCRIPTION = "The xkbcomp keymap compiler converts a description of an \
XKB keymap into one of several output formats. The most common use for \
xkbcomp is to create a compiled keymap file (.xkm extension) which can \
be read directly by XKB-capable X servers or utilities."

LIC_FILES_CHKSUM = "file://COPYING;md5=08436e4f4476964e2e2dd7e7e41e076a"

DEPENDS += "libxkbfile"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "07df9790d30f9a369d20a418af784de0"
SRC_URI[sha256sum] = "748dc4cf58ac95684106bd9cf163ac6ab7de9a236faec02a6f4d4006d63a5736"
