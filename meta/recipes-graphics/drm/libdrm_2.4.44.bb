require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "b760b1f2ae4ec6452383164ec697cd5d"
SRC_URI[sha256sum] = "42b45ad15bb1bc52630a4b37b7afcfaea27e01b3c0b4791ef25d0f7b2456f6a2"

