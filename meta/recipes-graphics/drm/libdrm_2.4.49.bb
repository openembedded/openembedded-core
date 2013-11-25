require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "454fe27645152cbd8be8ccda21acaa24"
SRC_URI[sha256sum] = "25d7d3fd30d8c350d3b87b3048f1c0cf0be295a40197a49acc374d4f4ae97a7d"

