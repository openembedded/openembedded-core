require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

PR = "r0"

SRC_URI += "file://installtests.patch"

SRC_URI[md5sum] = "96d5e3e9edd55f4b016fe3b5dd0a1953"
SRC_URI[sha256sum] = "e2432dc93e933479132123a1dca382294c30f55bc895bb737b6bdd6f2b8c452e"
