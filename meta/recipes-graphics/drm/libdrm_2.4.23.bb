require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

SRC_URI += "file://installtests.patch"
SRC_URI[md5sum] = "7577ff36ec364d88fae466d4f7fc5fc6"
SRC_URI[sha256sum] = "c0f06d68c3edba7a1ad937f5481a8c287efd4cd368cee66cd9e678b06a911c18"

PR = "r0"
