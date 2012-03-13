require xorg-util-common.inc

SUMMARY = "Data files for the imake utility"
DESCRIPTION = "Data files for the imake utility"
LIC_FILES_CHKSUM = "file://COPYING;md5=0f334a06f2de517e37e86d6757167d88"

DEPENDS = "font-util"

PR = "r1"

FILES_${PN} += "${libdir}/X11/config/*"

SRC_URI[md5sum] = "ff4502b6e31aac90e24ce134090d0e46"
SRC_URI[sha256sum] = "8fc8a1224d2a716b1f3f1ca85dfda02387ab215251b8eddd03551eac998c9cb8"

BBCLASSEXTEND = "native"
