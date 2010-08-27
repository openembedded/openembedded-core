require xorg-proto-common.inc

PR = "r2"
PE = "1"

DEPENDS += "gettext"

DESCRIPTION = "X.Org BigReqs extension headers"

BBCLASSEXTEND = "native nativesdk"

LIC_FILES_CHKSUM = "file://COPYING;md5=b12715630da6f268d0d3712ee1a504f4"
