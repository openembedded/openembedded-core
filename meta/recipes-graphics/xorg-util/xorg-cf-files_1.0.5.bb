require xorg-util-common.inc

SUMMARY = "Data files for the imake utility"
DESCRIPTION = "Data files for the imake utility"
LIC_FILES_CHKSUM = "file://COPYING;md5=0f334a06f2de517e37e86d6757167d88"

DEPENDS = "font-util"


FILES_${PN} += "${libdir}/X11/config/*"

SRC_URI[md5sum] = "ccb5f341ed5932d5ae870d9128e37c32"
SRC_URI[sha256sum] = "8c9ce7952094d49a1d3f849e45caf848d83ad879d91f3169cc373f52502d0c20"

BBCLASSEXTEND = "native"
