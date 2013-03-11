require xorg-driver-video.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=d8cbd99fff773f92e844948f74ef0df8"

DESCRIPTION = "X.Org X server -- fbdev display driver"
PR = "${INC_PR}.0"

SRC_URI += "file://Remove-mibstore.h.patch"

SRC_URI[md5sum] = "1d99f1dfb3f0fea077b6b61caa3dc85a"
SRC_URI[sha256sum] = "ff7b037ad110040a4e2db5c84e9741125dbbaf3a08107db47760f3e11f9c4831"
