require xorg-proto-common.inc

PR = "r0"
PE = "1"

DEPENDS += "gettext"

DESCRIPTION = "X.Org BigReqs extension headers"

BBCLASSEXTEND = "native nativesdk"

LIC_FILES_CHKSUM = "file://COPYING;md5=b12715630da6f268d0d3712ee1a504f4"

SRC_URI[md5sum] = "6f6c24436c2b3ab235eb14a85b9aaacf"
SRC_URI[sha256sum] = "1c3b85872b58d215e8fbfdc7c36bb397d10053324a5df8722227d35254fff09a"
