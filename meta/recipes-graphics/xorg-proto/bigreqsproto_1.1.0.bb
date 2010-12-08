require xorg-proto-common.inc

PR = "r2"
PE = "1"

DEPENDS += "gettext"

DESCRIPTION = "X.Org BigReqs extension headers"

BBCLASSEXTEND = "native nativesdk"

LIC_FILES_CHKSUM = "file://COPYING;md5=b12715630da6f268d0d3712ee1a504f4"

SRC_URI[md5sum] = "d30c5dbf19ca6dffcd9788227ecff8c5"
SRC_URI[sha256sum] = "4864e12d3c5a99b0a9ee4704822455299345e6c65b23c688a4e4bf11481107bd"
