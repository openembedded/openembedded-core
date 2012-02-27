require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

PR = "r0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "b8cf744ec113c6028fe0975b1133b649"
SRC_URI[sha256sum] = "8fc7e0e5b2f9bf493447a4ef7adc49174a700824457774cb53c1b8f2da866af4"
