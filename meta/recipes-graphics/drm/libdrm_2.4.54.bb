require libdrm.inc

SRC_URI += "file://installtests.patch \
            file://GNU_SOURCE_definition.patch \
           "
SRC_URI[md5sum] = "56e98a9c2073c3fab7f95e003b657f46"
SRC_URI[sha256sum] = "d94001ebfbe80e1523d1228ee2df57294698d1c734fad9ccf53efde8932fe4e9"

