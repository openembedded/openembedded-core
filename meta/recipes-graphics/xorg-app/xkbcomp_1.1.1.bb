require xorg-app-common.inc

DESCRIPTION = "The X Keyboard Extension essentially replaces the core protocol definition of keyboard."

LIC_FILES_CHKSUM = "file://COPYING;md5=08436e4f4476964e2e2dd7e7e41e076a"

DEPENDS += "libxkbfile"

BBCLASSEXTEND = "native"
