require libdrm.inc

PR = "${INC_PR}.0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "9765919c28d4a54887576db3680137cc"
SRC_URI[sha256sum] = "e4ea39a901d4a8e59064f10f413bb037dad7790f7c16a5986e7cc1453b36488f"
