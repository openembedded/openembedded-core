require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "342886a137ddd9ed4341675d132388ad"
SRC_URI[sha256sum] = "1b0c28fd2f2b92d2df0a73d1aed88f43cb0dee1267aea6bc52ccb5fca5757a08"

