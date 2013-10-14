require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "e043d6d59328887b9e434f4d27aacc09"
SRC_URI[sha256sum] = "a98809a55ede4dac17416cac41f1f017114a97a7b6c2574f96a225ab1bd06074"

