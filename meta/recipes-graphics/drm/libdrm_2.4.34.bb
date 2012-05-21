require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

PR = "r0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "293cb2b31392d52caa02ab0861dfc2c9"
SRC_URI[sha256sum] = "a468570e7c85107b7c2f8eaceeebaa8f8a0da86482618f445bb74fa0b0d0f8d0"
