require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "92ce56e7533a9b2fcb5c8f32d305328b"
SRC_URI[sha256sum] = "3ef0a70c16080fb90c50e807b660b7353d82509c03647f6ecc00bbfa1caee208"

