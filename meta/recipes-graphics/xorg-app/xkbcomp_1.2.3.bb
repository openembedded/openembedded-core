require xorg-app-common.inc

SUMMARY = "A program to compile XKB keyboard description"

DESCRIPTION = "The xkbcomp keymap compiler converts a description of an \
XKB keymap into one of several output formats. The most common use for \
xkbcomp is to create a compiled keymap file (.xkm extension) which can \
be read directly by XKB-capable X servers or utilities."

LIC_FILES_CHKSUM = "file://COPYING;md5=08436e4f4476964e2e2dd7e7e41e076a"

DEPENDS += "libxkbfile"

SRC_URI[md5sum] = "35622a497894c1cff9182d42696c3e27"
SRC_URI[sha256sum] = "7598e8f44e2643c96d2e37aa44f344f6c48f1a262fc9207154353195b760cb16"

BBCLASSEXTEND = "native"

