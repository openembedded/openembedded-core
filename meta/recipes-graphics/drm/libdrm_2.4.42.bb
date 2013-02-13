require libdrm.inc

PR = "${INC_PR}.0"

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "

SRC_URI[md5sum] = "a6e2e26951bcc920b2049b65e86a659f"
SRC_URI[sha256sum] = "e0671082a77871c8474d3c0f09dbf5ff82d2429cd248088a691f7b484cbc76e1"
