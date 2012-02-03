require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

PR = "r1"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "9f57a68b2c0836b55ebcbc241f6ca175"
SRC_URI[sha256sum] = "cacea9c157ec824ad278a06f4910659b2f3ae69686518ece8d6967843cddcd56"
