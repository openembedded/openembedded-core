require libdrm.inc

LIC_FILES_CHKSUM = "file://xf86drm.c;beginline=9;endline=32;md5=c8a3b961af7667c530816761e949dc71"

PR = "r0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "a40f5293dc0a7b49d2a1e959d7d60194"
SRC_URI[sha256sum] = "c390aee132f05910edb09398b70e108c6b53f9b69b21914a9ea3165eed6f1b21"
