require xorg-util-common.inc

DESCRIPTION = "create dependencies in makefiles"
DEPENDS = "xproto util-macros"
PR = "r0"
PE = "1"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=43a6eda34b48ee821b3b66f4f753ce4f"

SRC_URI[md5sum] = "ec37ca8b810a40cdfb16a736b3360f6c"
SRC_URI[sha256sum] = "2d3466acc29b382a4368b30371f17a4083933281b97f8ef67fc8b785a60d52dc"

