require xorg-app-common.inc

SUMMARY = "X Keyboard Extensions"

DESCRIPTION = "The X Keyboard Extension essentially replaces the core protocol definition of keyboard."

LIC_FILES_CHKSUM = "file://COPYING;md5=08436e4f4476964e2e2dd7e7e41e076a"

DEPENDS += "libxkbfile"

SRC_URI += "file://cross-compile-fix.patch"

BBCLASSEXTEND = "native"

SRC_URI[md5sum] = "0f55995cd8da9b2d88553e1a2e17cd0a"
SRC_URI[sha256sum] = "2c64aa414755e764ca548ae5c93e95f7b5bbd5e01bca16bf226fd32bfae77ea4"
