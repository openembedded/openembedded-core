require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

SRC_URI += "file://installtests.patch"
SRC_URI[md5sum] = "3bdfa33f35d1c902e5115cceb5500c83"
SRC_URI[sha256sum] = "0bb0e594e4094d9000d80f38e96e8f640b6364f96cfef5b970cf4481443c6b3d"

PR = "r0"
