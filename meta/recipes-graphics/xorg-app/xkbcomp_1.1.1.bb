require xorg-app-common.inc

SUMMARY = "X Keyboard Extensions"

DESCRIPTION = "The X Keyboard Extension essentially replaces the core protocol definition of keyboard."

LIC_FILES_CHKSUM = "file://COPYING;md5=08436e4f4476964e2e2dd7e7e41e076a"

DEPENDS += "libxkbfile"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "38c387bacdc01038c8ac280588792bcf"
SRC_URI[sha256sum] = "9775bcfd43d9ffa41e2865e5b2c933f419bf983d7a529b3103656c76fd82e663"
