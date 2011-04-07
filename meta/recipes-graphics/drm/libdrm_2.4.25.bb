require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

SRC_URI += "file://installtests.patch"

SRC_URI[md5sum] = "f53dc4c72109b17908e4113c3b8addfe"
SRC_URI[sha256sum] = "51f99a815a18876977991bbc6f190607791d25a6e47a3269880ce7679dbd0e9f"

PR = "r0"
