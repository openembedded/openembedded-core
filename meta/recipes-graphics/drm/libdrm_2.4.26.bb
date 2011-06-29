require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

SRC_URI += "file://installtests.patch"

SRC_URI[md5sum] = "062569426773f69b11a47a7712bba770"
SRC_URI[sha256sum] = "b25b06ab5a077736044cbd9a3a05a9a23b873a0887ab1aaf93aa2b3218b2d3dc"

PR = "r0"
