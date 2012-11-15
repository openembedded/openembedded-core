require libdrm.inc

PR = "${INC_PR}.0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "626a3dc44a40ef37565b61732b688567"
SRC_URI[sha256sum] = "48c14e4600ea0fde522292893eb4055d2f6d86a69d093d379c827deaccffaa1f"
