require xorg-app-common.inc

SUMMARY = "A program to compile XKB keyboard description"

DESCRIPTION = "The xkbcomp keymap compiler converts a description of an \
XKB keymap into one of several output formats. The most common use for \
xkbcomp is to create a compiled keymap file (.xkm extension) which can \
be read directly by XKB-capable X servers or utilities."

LIC_FILES_CHKSUM = "file://COPYING;md5=08436e4f4476964e2e2dd7e7e41e076a"

DEPENDS += "libxkbfile"

SRC_URI[md5sum] = "885b4d8a7c8c7afb3312d31934cb3549"
SRC_URI[sha256sum] = "b7612527914402d091424a93bc16f0d4d8778b4a874171f3f3dc681c690e65eb"

BBCLASSEXTEND = "native"

