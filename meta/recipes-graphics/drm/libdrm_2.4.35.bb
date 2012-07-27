require libdrm.inc

PR = "${INC_PR}.0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "a40f5293dc0a7b49d2a1e959d7d60194"
SRC_URI[sha256sum] = "c390aee132f05910edb09398b70e108c6b53f9b69b21914a9ea3165eed6f1b21"
