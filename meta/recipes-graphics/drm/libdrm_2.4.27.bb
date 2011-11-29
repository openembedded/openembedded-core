require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

SRC_URI += "file://installtests.patch"

SRC_URI[md5sum] = "0fba4f42735cd3d24dd7a8cde0023fbd"
SRC_URI[sha256sum] = "ea6b04dd3298e36c7a43aadd5f80f48baeafe4caaabcf78b01dc919c5c757973"

PR = "r0"
